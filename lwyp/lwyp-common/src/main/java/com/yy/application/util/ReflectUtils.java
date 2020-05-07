package com.yy.application.util;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.*;

import static com.google.common.collect.Lists.newArrayList;


/**
 * @author: David.liu
 * @version: v1.0
 * @description:
 * @date: 2020年03月20日 10:58
 */
public class ReflectUtils {


    /**
     * 根据字段名调用对象的getter方法，如果字段类型为boolean，则方法名可能为is开头，也有可能只是以setFieleName的普通方法
     *
     * @param instance
     * @param fieldName
     * @return getter方法调用后的返回值
     */
    public static Object callGetMethod(Object instance, String fieldName) {
        Object result = null;
        try {
            Method getter = getter(instance.getClass(), fieldName);
            if (getter != null) {
                result = getter.invoke(instance);
            }
        } catch (Exception e) {
            throw ExceptionUtils.wrap2Runtime(e);
        }
        return result;
    }


    /**
     * 得到给写的类或其父类中声明的公共方法
     *
     * @param entityClass
     * @param methodName
     * @return
     * @throws NoSuchMethodException
     */
    public static Method getMethod(Class<?> entityClass, String methodName, Class<?>... type) throws NoSuchMethodException {
        try {
            Method m = entityClass.getMethod(methodName, type);
            if (m != null) {
                return m;
            }
        } catch (NoSuchMethodException ex) {
            if (entityClass.getSuperclass() != null && entityClass.getSuperclass() != Object.class) {
                return getMethod(entityClass.getSuperclass(), methodName, type);
            } else {
                throw ex;
            }
        }
        return null;
    }






    /**
     * 得到指定类的指定字段名的getter方法
     *
     * @param fieldName
     * @return
     * @author
     * @CreateDate 2012-7-27 下午04:34:31
     */
    public static Method getter(Class clazz, String fieldName) throws NoSuchMethodException {
        PropertyDescriptor pd = getPropertyDescriptor(clazz, fieldName);
        if (pd != null) {
            return pd.getReadMethod();
        }

        throw new NoSuchMethodException(clazz.getName() + "." + fieldName + "()");
    }


    public static PropertyDescriptor getPropertyDescriptor(Class<?> clazz, String propertyName) {
        return BeanUtils.getPropertyDescriptor(clazz, propertyName);
    }

    public static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) {
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(clazz);

        int idx = 0;
        int hasClass = 0;
        for (PropertyDescriptor pd : pds) {
            if ("class".equals(pd.getName())) {
                hasClass = 1;
                break;
            }
            idx++;
        }

        PropertyDescriptor[] raft = new PropertyDescriptor[pds.length - hasClass];

        if (hasClass == 0) {
            System.arraycopy(pds, 0, raft, 0, raft.length);
        } else if (idx == 0) {
            System.arraycopy(pds, 1, raft, 0, raft.length);
        } else {
            System.arraycopy(pds, 0, raft, 0, idx);

            System.arraycopy(pds, idx + 1, raft, idx, raft.length - idx);
        }

        return raft;
    }

    public static PropertyDescriptor[] getPropertyDescriptors4IncludedNames(Class<?> clazz, String[] includedNames) {
        List<PropertyDescriptor> rslt = newArrayList();
        for (String name : includedNames) {
            rslt.add(getPropertyDescriptor(clazz, name));
        }
        return rslt.toArray(new PropertyDescriptor[rslt.size()]);
    }

    public static PropertyDescriptor[] getPropertyDescriptors4ExcludedNames(Class<?> clazz, String[] excludedNames) {
        PropertyDescriptor[] pds = getPropertyDescriptors(clazz);
        if (excludedNames == null || excludedNames.length == 0) {
            return pds;
        }

        List<PropertyDescriptor> rslt = newArrayList();

        for (PropertyDescriptor pd : pds) {
            boolean exclude = false;

            for (String excludedName : excludedNames) {
                if (pd.getName().equals(excludedName)) {
                    exclude = true;
                    break;
                }
            }

            if (!exclude) {
                rslt.add(pd);
            }
        }

        return rslt.toArray(new PropertyDescriptor[rslt.size()]);
    }

    public static Field[] getDeclaredFields(Class<?> entityClass, String[] ignoreProperties, boolean containsParents, int filterModifierSet) {
        List<Field> fields = new LinkedList<>();
        List<String> excludeProps = Collections.emptyList();
        if (ignoreProperties != null) {
            excludeProps = Arrays.asList(ignoreProperties);
        }

        while (entityClass != null) {
            Field[] temp = entityClass.getDeclaredFields();
            for (Field f : temp) {
                if (hasModifiers(f, filterModifierSet)) {
                } else {
                    if (excludeProps.contains(f.getName())) {
                        continue;
                    }
                    fields.add(f);
                }
            }
            if (containsParents && entityClass.getSuperclass() != null && entityClass.getSuperclass() != Object.class) {
                entityClass = entityClass.getSuperclass();
            } else {
                entityClass = null;
            }
        }

        return fields.toArray(new Field[0]);
    }

    /**
     * 判断此Field是否有包含指定修饰符集的其中之一
     *
     * @param field
     * @param modifierSet 修饰符集
     * @return 是否包含指定修饰符集的其中之一，返回true
     */
    public static boolean hasModifiers(Field field, int modifierSet) {
        int mdfr = field.getModifiers();
        return (mdfr & modifierSet) != 0;
    }

}
