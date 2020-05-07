package com.yy.application.converter;

import com.yy.application.util.DateUtils;
import com.yy.application.util.ValueUtils;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * @author: David.liu
 * @version: v1.0
 * @description:
 * @date: 2020年05月6日 10:54
 */
public final class ValueConverterRegistry {
    private static ValueConverterRegistry instance = new ValueConverterRegistry();

    public static ValueConverterRegistry getInstance() {
        return instance;
    }

    private Map<Class<?>, ValueConverter<?>> converters = new HashMap<>();

    private ValueConverterRegistry() {
        register(Integer.class, new IntConverter());
        register(Byte.class, new ByteConverter());
        register(Float.class, new FloatConverter());
        register(Long.class, new LongConverter());
        register(Short.class, new ShortConverter());
        register(Double.class, new DoubleConverter());
        register(BigDecimal.class, new BigDecimalConverter());
        register(BigInteger.class, new BigIntegerConverter());
        register(Character.class, new CharacterConverter());
        register(Boolean.class, new BooleanConverter());

        register(int.class, new IntConverter());
        register(byte.class, new ByteConverter());
        register(float.class, new FloatConverter());
        register(long.class, new LongConverter());
        register(short.class, new ShortConverter());
        register(double.class, new DoubleConverter());
        register(char.class, new CharacterConverter());
        register(boolean.class, new BooleanConverter());
        register(String.class, new StringConverter());
        register(Array.class, new ArrayConverter());
        register(List.class, new ListConverter());
        register(Set.class, new SetConverter());
    }

    public void register(Class<?> type, ValueConverter<?> converter) {
        converters.put(type, converter);
    }

    public void unregister(Class<?> type) {
        converters.remove(type);
    }

    public Map<Class<?>, ValueConverter<?>> getConverters() {
        return converters;
    }

    private static abstract class NumberConverter<T> implements ValueConverter<T> {
        @Override
        public T convert(Object value, Class<?> type, Object... extraParams) {
            if (value instanceof String) {
                return convertString((String) value);
            }
            if (value instanceof Date) {
                return convertString(((Date) value).getTime() + "");
            }
            if (value instanceof Boolean) {
                return convertNumber((Boolean) value ? 1 : 0);
            }
            if (value instanceof Number) {
                return convertNumber((Number) value);
            }
            return null;
        }

        protected abstract T convertString(String s);

        protected abstract T convertNumber(Number n);
    }

    private static class IntConverter extends NumberConverter<Integer> {
        @Override
        protected Integer convertString(String s) {
            return Integer.valueOf(s);
        }

        @Override
        protected Integer convertNumber(Number n) {
            return n.intValue();
        }
    }

    private static class FloatConverter extends NumberConverter<Float> {
        @Override
        protected Float convertString(String s) {
            return Float.valueOf(s);
        }

        @Override
        protected Float convertNumber(Number n) {
            return n.floatValue();
        }
    }

    private static class ShortConverter extends NumberConverter<Short> {
        @Override
        protected Short convertString(String s) {
            return Short.valueOf(s);
        }

        @Override
        protected Short convertNumber(Number n) {
            return n.shortValue();
        }
    }

    private static class ByteConverter extends NumberConverter<Byte> {
        @Override
        protected Byte convertString(String s) {
            return Byte.valueOf(s);
        }

        @Override
        protected Byte convertNumber(Number n) {
            return n.byteValue();
        }
    }

    private static class LongConverter extends NumberConverter<Long> {
        @Override
        protected Long convertString(String s) {
            return Long.valueOf(s);
        }

        @Override
        protected Long convertNumber(Number n) {
            return n.longValue();
        }
    }

    private static class DoubleConverter extends NumberConverter<Double> {
        @Override
        protected Double convertString(String s) {
            return Double.valueOf(s);
        }

        @Override
        protected Double convertNumber(Number n) {
            return n.doubleValue();
        }
    }

    public static class BigDecimalConverter extends NumberConverter<BigDecimal> {
        @Override
        protected BigDecimal convertString(String s) {
            return new BigDecimal(s);
        }

        @Override
        protected BigDecimal convertNumber(Number n) {
            return new BigDecimal(n.toString());
        }
    }

    public static class BigIntegerConverter extends NumberConverter<BigInteger> {
        @Override
        protected BigInteger convertString(String s) {
            return new BigInteger(s);
        }

        @Override
        protected BigInteger convertNumber(Number n) {
            return new BigInteger(n.toString());
        }
    }

    private static class CharacterConverter implements ValueConverter<Character> {
        @Override
        public Character convert(Object value, Class<?> type, Object... extraParams) {
            boolean booleanType = boolean.class.isAssignableFrom(value.getClass());
            if (value instanceof Boolean || booleanType) {
                boolean bool = false;
                if (value instanceof Boolean) {
                    bool = (Boolean) value;
                }
                if (booleanType) {
                    bool = (Boolean) value;
                }
                int c = bool ? 1 : 0;
                return (char) c;
            }
            return value.toString().charAt(0);
        }
    }

    private static class StringConverter implements ValueConverter<String> {
        @Override
        public String convert(Object value, Class<?> type, Object... extraParams) {
            if (value instanceof Date) {
                return DateUtils.date2Str((Date) value);
            }
            if (value instanceof Double) {
                Double number = (Double) value;
                return BigDecimal.valueOf(number).toString();
            }
            if (value instanceof Long) {
                Long number = (Long) value;
                return BigDecimal.valueOf(number).toString();
            }
            return value.toString();
        }
    }

    private static class BooleanConverter implements ValueConverter<Boolean> {
        @Override
        public Boolean convert(Object value, Class<?> type, Object... extraParams) {
            if ("1".equals(value.toString())) {
                return Boolean.TRUE;
            }
            if ("Y".equals(value.toString())) {
                return Boolean.TRUE;
            }
            return Boolean.valueOf(value.toString());
        }
    }

    public static class ArrayConverter implements ValueConverter<Object> {

        @Override
        public Object convert(Object value, Class<?> type, Object... extraParams) {
            Object arg0 = extraParams.length > 0 ? extraParams[0] : null;

            if (value instanceof String) {
                String split = arg0 instanceof String ? (String) arg0 : ",";
                value = ((String) value).split(split);
            }
            Class<?> elementType = type.getComponentType();

            if (value.getClass().isArray()) {
                if (elementType == value.getClass().getComponentType()) {
                    return value;
                }

                int length = Array.getLength(value);
                Object rslt = Array.newInstance(elementType, length);

                int index = 0;
                for (; index < length; index++) {
                    Object e = Array.get(value, index);
                    Object v = ValueUtils.convert(e, elementType);
                    Array.set(rslt, index, v);
                }

                return rslt;
            } else if (value instanceof Collection<?>) {
                Collection<?> coll = (Collection<?>) value;

                Object rslt = Array.newInstance(elementType, coll.size());

                int index = 0;
                for (Object e : coll) {
                    Object v = ValueUtils.convert(e, elementType);
                    Array.set(rslt, index++, v);
                }

                return rslt;
            }

            return null;
        }

    }

    @SuppressWarnings("rawtypes")
    private static abstract class CollectionConverter<T extends Collection> implements ValueConverter<T> {
        @SuppressWarnings("unchecked")
        @Override
        public T convert(Object value, Class<?> type, Object... extraParams) {
            T rslt = getCollection();

            Object arg0 = extraParams.length > 0 ? extraParams[0] : null;

            if (value instanceof String) {
                String split = arg0 instanceof String ? (String) arg0 : ",";
                value = ((String) value).split(split);
            }
            Class<?> elementType = null;
            if (arg0 != null) {
                if (arg0 instanceof Class) {
                    elementType = (Class) arg0;
                } else {
                    elementType = extraParams.length > 1 ? (Class<?>) extraParams[1] : null;
                }
            }

            if (value.getClass().isArray()) {
                int length = Array.getLength(value);
                for (int i = 0; i < length; i++) {
                    Object e = Array.get(value, i);
                    Object v = ValueUtils.convert(e, elementType);
                    rslt.add(v);
                }
            } else if (value instanceof Collection<?>) {
                Collection<?> coll = (Collection<?>) value;
                for (Object e : coll) {
                    Object v = ValueUtils.convert(e, elementType);
                    rslt.add(v);
                }
            }

            return rslt;
        }

        protected abstract T getCollection();

    }

    private static class ListConverter extends CollectionConverter<List<?>> {
        @Override
        protected List<?> getCollection() {
            return new ArrayList<>();
        }
    }

    private static class SetConverter extends CollectionConverter<Set<?>> {
        @Override
        protected Set<?> getCollection() {
            return new HashSet<>();
        }
    }
}