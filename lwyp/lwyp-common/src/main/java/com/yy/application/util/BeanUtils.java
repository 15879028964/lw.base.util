package com.yy.application.util;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.util.Assert;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author: David.liu
 * @version: v1.0
 * @description:
 * @date: 2020年05月6日 10:54
 */
public final class BeanUtils {

    private final static Map<String, BeanCopier> COPIER_MAP = new HashMap<>();


    /**
     * 得到给定实体类的所有字段名，以逗号分隔
     *
     * @param entityClass 实体类
     * @param excludes    要排除的字段列表
     * @return 返回逗号分隔的entityClass的所有声明字段，排除excludes指定的字段
     */
    public static Field[] getFields(Class<?> entityClass, String... excludes) {
        return getFields(entityClass, Modifier.STATIC, true, excludes);
    }

    public static Field[] getFields(Class<?> entityClass, int filterModifierSet, boolean containsParent, String... excludes) {
        Field[] fs = ReflectUtils.getDeclaredFields(entityClass, excludes, containsParent, filterModifierSet);
        List<Field> rslt = new ArrayList<>();
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            if (contains(f.getName(), excludes)) {
                continue;
            }
            rslt.add(f);
        }
        return rslt.toArray(new Field[rslt.size()]);
    }

    private static boolean contains(String f, String... fs) {
        if (fs == null || fs.length == 0) {
            return false;
        }
        boolean result = false;
        for (String f1 : fs) {
            if (f1.contains(",")) {
                return contains(f, f1.split(","));
            } else {
                if (f.equals(f1)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }


    private static List<Map<String, Object>> doBeans2Maps(List<? extends Object> beans, PropertyDescriptor[] pds) {
        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>(beans.size());

        for (Object bean : beans) {
            Map<String, Object> map = new HashMap<String, Object>(pds.length);

            doBean2Map(bean, map, pds);

            maps.add(map);
        }

        return maps;
    }


    public static void bean2Map(Object obj, Map<String, Object> map, String... ignoreProperties) {
        if (obj == null) {
            return;
        }

        PropertyDescriptor[] pds = ReflectUtils.getPropertyDescriptors4ExcludedNames(obj.getClass(), ignoreProperties);

        doBean2Map(obj, map, pds);
    }


    private static void doBean2Map(Object bean, Map<String, Object> map, PropertyDescriptor[] pds) {
        try {
            for (PropertyDescriptor pd : pds) {
                Method getter = pd.getReadMethod();
                if (getter != null && Modifier.isPublic(getter.getModifiers()) && !Modifier.isStatic(getter.getModifiers())) {
                    map.put(pd.getName(), getter.invoke(bean));
                }
            }
        } catch (Exception e) {
            throw ExceptionUtils.wrap2Runtime(e);
        }
    }

    public static void map2Bean(Map<String, Object> data, Object obj, String... ignoreKeys) {
        try {
            Set<String> ignoreSet = getSet(ignoreKeys);
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                if (ignoreSet.contains(entry.getKey())) {
                    continue;
                }
                PropertyDescriptor pd = ReflectUtils.getPropertyDescriptor(obj.getClass(), entry.getKey());
                if (pd != null) {
                    Object value = entry.getValue();
                    Method setter = pd.getWriteMethod();
                    if (setter != null && Modifier.isPublic(setter.getModifiers()) && !Modifier.isStatic(setter.getModifiers())) {
                        setter.invoke(obj, ValueUtils.convert(value, pd.getPropertyType()));
                    }
                }
            }
        } catch (Exception e) {
            throw ExceptionUtils.wrap2Runtime(e);
        }
    }


    public static <T> List<T> copyList(List<?> source, Class<T> targetClass, String[] ignoreProperties) {
        Assert.notNull(source, "Parameter source is required");
        try {
            List<T> rslt = new ArrayList<T>();
            for (Object obj : source) {
                T target = targetClass.newInstance();
                copyProperties(obj, target, ignoreProperties);
                rslt.add(target);
            }
            return rslt;
        } catch (Exception e) {
            throw ExceptionUtils.wrap2Runtime(e);
        }
    }

    public static <T> T copyProperties(Object source, Class<T> targetClass, String[] ignoreProperties) {
        try {
            T target = targetClass.newInstance();
            copyProperties(source, target, ignoreProperties);
            return target;
        } catch (Exception e) {
            throw ExceptionUtils.wrap2Runtime(e);
        }
    }

    public static void copyProperties(Object source, Object target, String[] ignoreProperties) {
        Assert.notNull(source, "Parameter source is required");
        Assert.notNull(target, "Parameter target is required");

        try {
            Class srcClass = source.getClass();
            Class targetClass = target.getClass();

            boolean isSrcMap = Map.class.isAssignableFrom(srcClass);
            boolean isTargetMap = Map.class.isAssignableFrom(targetClass);

            if (!isSrcMap) {
                if (isTargetMap) {
                    bean2Map(source, (Map<String, Object>) target, ignoreProperties);
                } else {
                    doCopyProperties(source, target, ignoreProperties);
                }
            } else {
                Map<String, Object> sourceMap = (Map<String, Object>) source;

                if (isTargetMap) {
                    Map<String, Object> targetMap = (Map<String, Object>) target;
                    Set<String> ignoreSet = getSet(ignoreProperties);

                    for (Map.Entry<String, Object> entry : sourceMap.entrySet()) {
                        if (ignoreSet.contains(entry.getKey())) {
                            continue;
                        }
                        targetMap.put(entry.getKey(), entry.getValue());
                    }
                } else {
                    map2Bean(sourceMap, target, ignoreProperties);
                }
            }
        } catch (Exception e) {
            throw ExceptionUtils.wrap2Runtime(e);
        }
    }

    private static void doCopyProperties(Object source, Object target, String[] ignoreProperties) throws Exception {
        if (ignoreProperties == null || ignoreProperties.length == 0) {
            try {
                String copierKey = source.getClass().getName() + "|" + target.getClass().getName();
                BeanCopier copier = COPIER_MAP.get(copierKey);
                if (copier == null) {
                    copier = BeanCopier.create(source.getClass(), target.getClass(), true);
                    COPIER_MAP.put(copierKey, copier);
                }
                copier.copy(source, target, (value, target1, context) -> ValueUtils.convert(value, target1));
            } catch (Throwable t) {
                System.err.println(t.getMessage());
                doCopyPropertiesByReflection(source, target, ignoreProperties);
            }
        } else {
            doCopyPropertiesByReflection(source, target, ignoreProperties);
        }
    }

    private static void doCopyPropertiesByReflection(Object source, Object target, String[] ignoreProperties) throws Exception {
        Set<String> ignoreSet = getSet(ignoreProperties);

        PropertyDescriptor[] pds = ReflectUtils.getPropertyDescriptors(source.getClass());

        for (PropertyDescriptor pd : pds) {
            if (ignoreSet.contains(pd.getName())) {
                continue;
            }

            Method readMethod = pd.getReadMethod();

            if (readMethod != null && Modifier.isPublic(readMethod.getModifiers()) && !Modifier.isStatic(readMethod.getModifiers())) {
                PropertyDescriptor tgtPd = ReflectUtils.getPropertyDescriptor(target.getClass(), pd.getName());

                if (tgtPd == null) {
                    continue;
                }

                Method writeMethod = tgtPd.getWriteMethod();

                if (writeMethod != null && Modifier.isPublic(writeMethod.getModifiers()) && !Modifier.isStatic(writeMethod.getModifiers())) {
                    Object value = readMethod.invoke(source);

                    if (value != null) {
                        writeMethod.invoke(target, ValueUtils.convert(value, tgtPd.getPropertyType()));
                    }
                }
            }
        }
    }

    private static Set<String> getSet(String[] array) {
        return array != null && array.length > 0 ? Sets.newHashSet(array) : Collections.emptySet();
    }


    public static List<String> getNonNullFields(Object obj, String[] fields, String... excludeFields) {
        List<String> nonNullFields = Lists.newArrayList();

        List<String> excludes = Collections.emptyList();
        if (excludeFields != null) {
            excludes = Arrays.asList(excludeFields);
        }
        for (String getter : fields) {
            if (excludes.contains(getter)) {
                continue;
            }
            Object value = ReflectUtils.callGetMethod(obj, getter);
            if (value != null) {
                nonNullFields.add(getter);
            }
        }
        return nonNullFields;
    }

    /**
     * 复制source实例的字段到target实例，同时排除source的父类，父类的父类（假如有）一直到祖先类的字段，除了exceptParentFields指定的字段，其他父类字段都会被排除
     *
     * @param source
     * @param target
     * @param exceptParentFields 指定不排除的父类字段，即这些字段不会被排除
     */
    public static void copyPropertiesIgnoreParents(Object source, Object target, String... exceptParentFields) {
        String[] fieldNames = getIgnoreParentFields(source.getClass().getSuperclass(), exceptParentFields);
        BeanUtils.copyProperties(source, target, fieldNames);
    }

    /**
     * 同{@code #copyPropertiesIgnoreParents(Object, Object, String...)}
     * 自动创建targetClass的实例，要求targetClass有空参构造方法
     *
     * @param source
     * @param targetClass
     * @param exceptParentFields
     * @return
     */
    public static <T> T copyPropertiesIgnoreParents(Object source, Class<T> targetClass, String... exceptParentFields) {
        String[] fieldNames = getIgnoreParentFields(source.getClass().getSuperclass(), exceptParentFields);
        return BeanUtils.copyProperties(source, targetClass, fieldNames);
    }

    /**
     * 复制一批source到一批targetClass类型的实例
     * 同{@code exceptParentFields(Object, Object, String...)}
     * 自动创建targetClass的实例，要求targetClass有空参构造方法
     *
     * @param sourceList
     * @param targetClass
     * @param exceptParentFields
     * @return
     */
    public static <T> List<T> copyListIgnoreParents(List<?> sourceList, Class<T> targetClass, String... exceptParentFields) {
        if (sourceList.isEmpty()) {
            return (List<T>) Collections.emptyList();
        }
        String[] fieldNames = getIgnoreParentFields(sourceList.get(0).getClass().getSuperclass(), exceptParentFields);
        return BeanUtils.copyList(sourceList, targetClass, fieldNames);
    }

    private static String[] getIgnoreParentFields(Class<?> superClass, String... exceptParentFields) {
        Field[] fields = BeanUtils.getFields(superClass.getSuperclass(), exceptParentFields);
        String[] fieldNames = new String[fields.length];
        int idx = 0;
        for (Field field : fields) {
            fieldNames[idx++] = field.getName();
        }
        return fieldNames;
    }

    public static Object deepClone(Object obj) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(obj);
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            return in.readObject();
        } catch (Exception e) {
            throw ExceptionUtils.wrap2Runtime(e);
        }
    }
}
