/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//beanutils/src/java/org/apache/commons/beanutils/ConvertUtilsBean.java,v 1.6 2003/05/06 11:32:23 rdonkin Exp $
 * $Revision: 1.6 $
 * $Date: 2003/05/06 11:32:23 $
 *
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */


package org.apache.commons.beanutils;


import java.io.File;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.BigIntegerConverter;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.BooleanArrayConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.ByteArrayConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.CharacterArrayConverter;
import org.apache.commons.beanutils.converters.ClassConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.DoubleArrayConverter;
import org.apache.commons.beanutils.converters.FileConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.FloatArrayConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.IntegerArrayConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.LongArrayConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.beanutils.converters.ShortArrayConverter;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.beanutils.converters.SqlTimeConverter;
import org.apache.commons.beanutils.converters.SqlTimestampConverter;
import org.apache.commons.beanutils.converters.StringConverter;
import org.apache.commons.beanutils.converters.StringArrayConverter;
import org.apache.commons.beanutils.converters.URLConverter;
import org.apache.commons.collections.FastHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <p>Utility methods for converting String scalar values to objects of the
 * specified Class, String arrays to arrays of the specified Class.  The
 * actual {@link Converter} instance to be used can be registered for each
 * possible destination Class.  Unless you override them, standard
 * {@link Converter} instances are provided for all of the following
 * destination Classes:</p>
 * <ul>
 * <li>java.lang.BigDecimal</li>
 * <li>java.lang.BigInteger</li>
 * <li>boolean and java.lang.Boolean</li>
 * <li>byte and java.lang.Byte</li>
 * <li>char and java.lang.Character</li>
 * <li>java.lang.Class</li>
 * <li>double and java.lang.Double</li>
 * <li>float and java.lang.Float</li>
 * <li>int and java.lang.Integer</li>
 * <li>long and java.lang.Long</li>
 * <li>short and java.lang.Short</li>
 * <li>java.lang.String</li>
 * <li>java.io.File</li>
 * <li>java.net.URL</li>
 * <li>java.sql.Date</li>
 * <li>java.sql.Time</li>
 * <li>java.sql.Timestamp</li>
 * </ul>
 *
 * <p>For backwards compatibility, the standard Converters for primitive
 * types (and the corresponding wrapper classes) return a defined
 * default value when a conversion error occurs.  If you prefer to have a
 * {@link ConversionException} thrown instead, replace the standard Converter
 * instances with instances created with the zero-arguments constructor.  For
 * example, to cause the Converters for integers to throw an exception on
 * conversion errors, you could do this:</p>
 * <pre>
 *   // No-args constructor gets the version that throws exceptions
 *   Converter myConverter =
 *    new org.apache.commons.beanutils.converter.IntegerConverter();
 *   ConvertUtils.register(myConverter, Integer.TYPE);    // Native type
 *   ConvertUtils.register(myConverter, Integer.class);   // Wrapper class
 * </pre>
 *
 * @author Craig R. McClanahan
 * @author Ralph Schaer
 * @author Chris Audley
 * @author James Strachan
 * @version $Revision: 1.6 $ $Date: 2003/05/06 11:32:23 $
 * @since 1.7
 */

public class ConvertUtilsBean {
    
    // ------------------------------------------------------- Class Variables
    /** Default singleton */
    private static final ConvertUtilsBean singleton = new ConvertUtilsBean();
    
    // ------------------------------------------------------- Class Methods
    /** Get singleton instance */
    protected static ConvertUtilsBean getInstance() {
        return singleton;
    }

    // ------------------------------------------------------- Variables


    /**
     * The set of {@link Converter}s that can be used to convert Strings
     * into objects of a specified Class, keyed by the destination Class.
     */
    private FastHashMap converters = new FastHashMap();

    /**
     * The <code>Log</code> instance for this class.
     */
    private Log log = LogFactory.getLog(ConvertUtils.class);

    // ------------------------------------------------------- Constructors

    /** Construct a bean with standard converters registered */
    public ConvertUtilsBean() {
        converters.setFast(false);
        deregister();
        converters.setFast(true);
    }

    // --------------------------------------------------------- Public Methods
    
    // ------------------------------------------------------ Static Properties


    /**
     * The default value for Boolean conversions.
     * @deprecated Register replacement converters for Boolean.TYPE and
     *  Boolean.class instead
     */
    private Boolean defaultBoolean = Boolean.FALSE;

    /**
     * Gets the default value for Boolean conversions.
     * @deprecated Register replacement converters for Boolean.TYPE and
     *  Boolean.class instead
     */
    public boolean getDefaultBoolean() {
        return (defaultBoolean.booleanValue());
    }

    /**
     * Sets the default value for Boolean conversions.
     * @deprecated Register replacement converters for Boolean.TYPE and
     *  Boolean.class instead
     */
    public void setDefaultBoolean(boolean newDefaultBoolean) {
        defaultBoolean = new Boolean(newDefaultBoolean);
        register(new BooleanConverter(defaultBoolean), Boolean.TYPE);
        register(new BooleanConverter(defaultBoolean), Boolean.class);
    }


    /**
     * The default value for Byte conversions.
     * @deprecated Register replacement converters for Byte.TYPE and
     *  Byte.class instead
     */
    private Byte defaultByte = new Byte((byte) 0);

    /**
     * Gets the default value for Byte conversions.
     * @deprecated Register replacement converters for Byte.TYPE and
     *  Byte.class instead
     */
    public byte getDefaultByte() {
        return (defaultByte.byteValue());
    }

    /**
     * Sets the default value for Byte conversions.
     * @deprecated Register replacement converters for Byte.TYPE and
     *  Byte.class instead
     */
    public void setDefaultByte(byte newDefaultByte) {
        defaultByte = new Byte(newDefaultByte);
        register(new ByteConverter(defaultByte), Byte.TYPE);
        register(new ByteConverter(defaultByte), Byte.class);
    }


    /**
     * The default value for Character conversions.
     * @deprecated Register replacement converters for Character.TYPE and
     *  Character.class instead
     */
    private Character defaultCharacter = new Character(' ');

    /**
     * Gets the default value for Character conversions.
     * @deprecated Register replacement converters for Character.TYPE and
     *  Character.class instead
     */
    public char getDefaultCharacter() {
        return (defaultCharacter.charValue());
    }

    /**
     * Sets the default value for Character conversions.
     * @deprecated Register replacement converters for Character.TYPE and
     *  Character.class instead
     */
    public void setDefaultCharacter(char newDefaultCharacter) {
        defaultCharacter = new Character(newDefaultCharacter);
        register(new CharacterConverter(defaultCharacter),
                    Character.TYPE);
        register(new CharacterConverter(defaultCharacter),	
                    Character.class);
    }


    /**
     * The default value for Double conversions.
     * @deprecated Register replacement converters for Double.TYPE and
     *  Double.class instead
     */
    private Double defaultDouble = new Double((double) 0.0);

    /**
     * Gets the default value for Double conversions.
     * @deprecated Register replacement converters for Double.TYPE and
     *  Double.class instead
     */
    public double getDefaultDouble() {
        return (defaultDouble.doubleValue());
    }

    /**
     * Sets the default value for Double conversions.
     * @deprecated Register replacement converters for Double.TYPE and
     *  Double.class instead
     */
    public void setDefaultDouble(double newDefaultDouble) {
        defaultDouble = new Double(newDefaultDouble);
        register(new DoubleConverter(defaultDouble), Double.TYPE);
        register(new DoubleConverter(defaultDouble), Double.class);
    }


    /**
     * The default value for Float conversions.
     * @deprecated Register replacement converters for Float.TYPE and
     *  Float.class instead
     */
    private Float defaultFloat = new Float((float) 0.0);

    /**
     * Gets the default value for Float conversions.
     * @deprecated Register replacement converters for Float.TYPE and
     *  Float.class instead
     */
    public float getDefaultFloat() {
        return (defaultFloat.floatValue());
    }

    /**
     * Sets the default value for Float conversions.
     * @deprecated Register replacement converters for Float.TYPE and
     *  Float.class instead
     */
    public void setDefaultFloat(float newDefaultFloat) {
        defaultFloat = new Float(newDefaultFloat);
        register(new FloatConverter(defaultFloat), Float.TYPE);
        register(new FloatConverter(defaultFloat), Float.class);
    }


    /**
     * The default value for Integer conversions.
     * @deprecated Register replacement converters for Integer.TYPE and
     *  Integer.class instead
     */
    private Integer defaultInteger = new Integer(0);

    /**
     * Gets the default value for Integer conversions.
     * @deprecated Register replacement converters for Integer.TYPE and
     *  Integer.class instead
     */
    public int getDefaultInteger() {
        return (defaultInteger.intValue());
    }
    
    /**
     * Sets the default value for Integer conversions.
     * @deprecated Register replacement converters for Integer.TYPE and
     *  Integer.class instead
     */
    public void setDefaultInteger(int newDefaultInteger) {
        defaultInteger = new Integer(newDefaultInteger);
        register(new IntegerConverter(defaultInteger), Integer.TYPE);
        register(new IntegerConverter(defaultInteger), Integer.class);
    }


    /**
     * The default value for Long conversions.
     * @deprecated Register replacement converters for Long.TYPE and
     *  Long.class instead
     */
    private Long defaultLong = new Long((long) 0);

    /**
     * Gets the default value for Long conversions.
     * @deprecated Register replacement converters for Long.TYPE and
     *  Long.class instead
     */
    public long getDefaultLong() {
        return (defaultLong.longValue());
    }

    /**
     * Sets the default value for Long conversions.
     * @deprecated Register replacement converters for Long.TYPE and
     *  Long.class instead
     */
    public void setDefaultLong(long newDefaultLong) {
        defaultLong = new Long(newDefaultLong);
        register(new LongConverter(defaultLong), Long.TYPE);
        register(new LongConverter(defaultLong), Long.class);
    }


    /**
     * The default value for Short conversions.
     * @deprecated Register replacement converters for Short.TYPE and
     *  Short.class instead
     */
    private static Short defaultShort = new Short((short) 0);

    /**
     * Gets the default value for Short conversions.
     * @deprecated Register replacement converters for Short.TYPE and
     *  Short.class instead
     */
    public short getDefaultShort() {
        return (defaultShort.shortValue());
    }

    /**
     * Sets the default value for Short conversions.
     * @deprecated Register replacement converters for Short.TYPE and
     *  Short.class instead
     */
    public void setDefaultShort(short newDefaultShort) {
        defaultShort = new Short(newDefaultShort);
        register(new ShortConverter(defaultShort), Short.TYPE);
        register(new ShortConverter(defaultShort), Short.class);
    }



    /**
     * Convert the specified value into a String.  If the specified value
     * is an array, the first element (converted to a String) will be
     * returned.  The registered {@link Converter} for the
     * <code>java.lang.String</code> class will be used, which allows
     * applications to customize Object->String conversions (the default
     * implementation simply uses toString()).
     *
     * @param value Value to be converted (may be null)
     */
    public String convert(Object value) {

        if (value == null) {
            return ((String) null);
        } else if (value.getClass().isArray()) {
            if (Array.getLength(value) < 1) {
                return (null);
            }
            value = Array.get(value, 0);
            if (value == null) {
                return ((String) null);
            } else {
                Converter converter = (Converter) converters.get(String.class);
                return ((String) converter.convert(String.class, value));
            }
        } else {
            Converter converter = (Converter) converters.get(String.class);
            return ((String) converter.convert(String.class, value));
        }

    }


    /**
     * Convert the specified value to an object of the specified class (if
     * possible).  Otherwise, return a String representation of the value.
     *
     * @param value Value to be converted (may be null)
     * @param clazz Java class to be converted to
     *
     * @exception ConversionException if thrown by an underlying Converter
     */
    public Object convert(String value, Class clazz) {

        if (log.isDebugEnabled()) {
            log.debug("Convert string '" + value + "' to class '" +
                      clazz.getName() + "'");
        }
        Converter converter = (Converter) converters.get(clazz);
        if (converter == null) {
            converter = (Converter) converters.get(String.class);
        }
        if (log.isTraceEnabled()) {
            log.trace("  Using converter " + converter);
        }
        return (converter.convert(clazz, value));

    }


    /**
     * Convert an array of specified values to an array of objects of the
     * specified class (if possible).  If the specified Java class is itself
     * an array class, this class will be the type of the returned value.
     * Otherwise, an array will be constructed whose component type is the
     * specified class.
     *
     * @param value Value to be converted (may be null)
     * @param clazz Java array or element class to be converted to
     *
     * @exception ConversionException if thrown by an underlying Converter
     */
    public Object convert(String values[], Class clazz) {

        Class type = clazz;
        if (clazz.isArray()) {
            type = clazz.getComponentType();
        }
        if (log.isDebugEnabled()) {
            log.debug("Convert String[" + values.length + "] to class '" +
                      type.getName() + "[]'");
        }
        Converter converter = (Converter) converters.get(type);
        if (converter == null) {
            converter = (Converter) converters.get(String.class);
        }
        if (log.isTraceEnabled()) {
            log.trace("  Using converter " + converter);
        }
        Object array = Array.newInstance(type, values.length);
        for (int i = 0; i < values.length; i++) {
            Array.set(array, i, converter.convert(type, values[i]));
        }
        return (array);

    }


    /**
     * Remove all registered {@link Converter}s, and re-establish the
     * standard Converters.
     */
    public void deregister() {

        boolean booleanArray[] = new boolean[0];
        byte byteArray[] = new byte[0];
        char charArray[] = new char[0];
        double doubleArray[] = new double[0];
        float floatArray[] = new float[0];
        int intArray[] = new int[0];
        long longArray[] = new long[0];
        short shortArray[] = new short[0];
        String stringArray[] = new String[0];

        converters.clear();
        converters.put(BigDecimal.class, new BigDecimalConverter());
        converters.put(BigInteger.class, new BigIntegerConverter());
        converters.put(Boolean.TYPE, new BooleanConverter(defaultBoolean));
        converters.put(Boolean.class,  new BooleanConverter(defaultBoolean));
        converters.put(booleanArray.getClass(),
                       new BooleanArrayConverter(booleanArray));
        converters.put(Byte.TYPE, new ByteConverter(defaultByte));
        converters.put(Byte.class, new ByteConverter(defaultByte));
        converters.put(byteArray.getClass(),
                       new ByteArrayConverter(byteArray));
        converters.put(Character.TYPE,
                       new CharacterConverter(defaultCharacter));
        converters.put(Character.class,
                       new CharacterConverter(defaultCharacter));
        converters.put(charArray.getClass(),
                       new CharacterArrayConverter(charArray));
        converters.put(Class.class, new ClassConverter());
        converters.put(Double.TYPE, new DoubleConverter(defaultDouble));
        converters.put(Double.class, new DoubleConverter(defaultDouble));
        converters.put(doubleArray.getClass(),
                       new DoubleArrayConverter(doubleArray));
        converters.put(Float.TYPE, new FloatConverter(defaultFloat));
        converters.put(Float.class, new FloatConverter(defaultFloat));
        converters.put(floatArray.getClass(),
                       new FloatArrayConverter(floatArray));
        converters.put(Integer.TYPE, new IntegerConverter(defaultInteger));
        converters.put(Integer.class, new IntegerConverter(defaultInteger));
        converters.put(intArray.getClass(),
                       new IntegerArrayConverter(intArray));
        converters.put(Long.TYPE, new LongConverter(defaultLong));
        converters.put(Long.class, new LongConverter(defaultLong));
        converters.put(longArray.getClass(),
                       new LongArrayConverter(longArray));
        converters.put(Short.TYPE, new ShortConverter(defaultShort));
        converters.put(Short.class, new ShortConverter(defaultShort));
        converters.put(shortArray.getClass(),
                       new ShortArrayConverter(shortArray));
        converters.put(String.class, new StringConverter());
        converters.put(stringArray.getClass(),
                       new StringArrayConverter(stringArray));
        converters.put(Date.class, new SqlDateConverter());
        converters.put(Time.class, new SqlTimeConverter());
        converters.put(Timestamp.class, new SqlTimestampConverter());
        converters.put(File.class, new FileConverter());
        converters.put(URL.class, new URLConverter());

    }


    /**
     * Remove any registered {@link Converter} for the specified destination
     * <code>Class</code>.
     *
     * @param clazz Class for which to remove a registered Converter
     */
    public void deregister(Class clazz) {

        converters.remove(clazz);

    }


    /**
     * Look up and return any registered {@link Converter} for the specified
     * destination class; if there is no registered Converter, return
     * <code>null</code>.
     *
     * @param clazz Class for which to return a registered Converter
     */
    public Converter lookup(Class clazz) {

        return ((Converter) converters.get(clazz));

    }


    /**
     * Register a custom {@link Converter} for the specified destination
     * <code>Class</code>, replacing any previously registered Converter.
     *
     * @param converter Converter to be registered
     * @param clazz Destination class for conversions performed by this
     *  Converter
     */
    public void register(Converter converter, Class clazz) {

        converters.put(clazz, converter);

    }
}
