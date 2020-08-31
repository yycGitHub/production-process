package com.surekam.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class BigDecimalUtils {

	   public static final int MONEY_POINT = 2; // 货币保留两位小数

	   /**
	    * 格式化精度
	    * 
	    * @param v
	    * @param point
	    *            小数位数
	    * @return double
	    */
	   public static Double format(double v, int point) {
	      BigDecimal b = new BigDecimal(v);
	      return b.setScale(point, BigDecimal.ROUND_HALF_UP).doubleValue();
	   }
	   
	   /**
	    * 
	    * @param v
	    * @param point
	    * @return
	    */
	   public static Double formatRoundUp(double v, int point) {
	      NumberFormat nf = NumberFormat.getInstance();
	        nf.setRoundingMode(RoundingMode.HALF_UP);//设置四舍五入
	        nf.setMinimumFractionDigits(point);//设置最小保留几位小数
	        nf.setMaximumFractionDigits(point);//设置最大保留几位小数
	        return Double.valueOf(nf.format(v));
	   }

	   /**
	    * 格式化金额。带千位符
	    * 
	    * @param v
	    * @return
	    */
	   public static String moneyFormat(Double v) {
	      DecimalFormat formater = new DecimalFormat();
	      formater.setMaximumFractionDigits(2);
	      formater.setGroupingSize(3);
	      formater.setRoundingMode(RoundingMode.FLOOR);
	      return formater.format(v.doubleValue());
	   }

	   /**
	    * 带小数的显示小数。不带小数的显示整数
	    * @param d
	    * @return
	    */
	   public static String doubleTrans(Double d) {
	      if (Math.round(d) - d == 0) {
	         return String.valueOf((long) d.doubleValue());
	      }
	      return String.valueOf(d);
	   }

	   /**
	    * BigDecimal 相加
	    * 
	    * @param v1
	    * @param v2
	    * @return double
	    */
	   public static Double add(double v1, double v2) {
	      BigDecimal n1 = new BigDecimal(Double.toString(v1));
	      BigDecimal n2 = new BigDecimal(Double.toString(v2));
	      return n1.add(n2).doubleValue();
	   }

	   /**
	    * BigDecimal 相减
	    * 
	    * @param v1
	    * @param v2
	    * @return double
	    */
	   public static Double subtract(double v1, double v2) {
	      BigDecimal n1 = new BigDecimal(Double.toString(v1));
	      BigDecimal n2 = new BigDecimal(Double.toString(v2));
	      return n1.subtract(n2).doubleValue();
	   }

	   /**
	    * BigDecimal 相乘
	    * 
	    * @param v1
	    * @param v2
	    * @return double
	    */
	   public static Double multiply(double v1, double v2) {
	      BigDecimal n1 = new BigDecimal(Double.toString(v1));
	      BigDecimal n2 = new BigDecimal(Double.toString(v2));
	      return n1.multiply(n2).doubleValue();
	   }

	   /**
	    * BigDecimal 相除
	    * 
	    * @param v1
	    * @param v2
	    * @return double
	    */
	   public static Double divide(double v1, double v2) {
	      BigDecimal n1 = new BigDecimal(Double.toString(v1));
	      BigDecimal n2 = new BigDecimal(Double.toString(v2));
	      return n1.divide(n2, 10, BigDecimal.ROUND_HALF_UP).doubleValue();
	   }

	   /**
	    * 比较大小 小于0：v1 < v2 大于0：v1 > v2 等于0：v1 = v2
	    * 
	    * @param v1
	    * @param v2
	    * @return
	    */
	   public static int compare(double v1, double v2) {
	      BigDecimal n1 = new BigDecimal(Double.toString(v1));
	      BigDecimal n2 = new BigDecimal(Double.toString(v2));
	      return n1.compareTo(n2);
	   }

	   /**
		 * BigDecimal的加法运算封装
		 * @param b1
		 * @param bn
		 * @return
		 */
	   public static BigDecimal safeAdd(BigDecimal b1, BigDecimal... bn) {
	       if (null == b1) {
	           b1 = BigDecimal.ZERO;
	       }
	       if (null != bn) {
	           for (BigDecimal b : bn) {
	               b1 = b1.add(null == b ? BigDecimal.ZERO : b);
	           }
	       }
	       return b1;
	   }
	 
	 
	   /**
	    * 计算金额方法
	    * @param b1
	    * @param bn
	    * @return
	    */
	   public static BigDecimal safeSubtract(BigDecimal b1, BigDecimal... bn) {
	       return safeSubtract(true, b1, bn);
	   }
	 
	   /**
	    * BigDecimal的安全减法运算
	    * @param isZero  减法结果为负数时是否返回0，true是返回0（金额计算时使用），false是返回负数结果
	    * @param b1		   被减数
	    * @param bn        需要减的减数数组
	    * @return
	    */
	   public static BigDecimal safeSubtract(Boolean isZero, BigDecimal b1, BigDecimal... bn) {
	       if (null == b1) {
	           b1 = BigDecimal.ZERO;
	       }
	       BigDecimal r = b1;
	       if (null != bn) {
	           for (BigDecimal b : bn) {
	               r = r.subtract((null == b ? BigDecimal.ZERO : b));
	           }
	       }
	       return isZero ? (r.compareTo(BigDecimal.ZERO) == -1 ? BigDecimal.ZERO : r) : r;
	   }
	 
	  
	 
	   /**
	    * 金额除法计算，返回2位小数（具体的返回多少位大家自己看着改吧）
	    * @param b1
	    * @param b2
	    * @return
	    */
	   public static <T extends Number> BigDecimal safeDivide(T b1, T b2){
	       return safeDivide(b1, b2, BigDecimal.ZERO);
	   }
	 
	   /**
	    * BigDecimal的除法运算封装，如果除数或者被除数为0，返回默认值
	    * 默认返回小数位后2位，用于金额计算
	    * @param b1
	    * @param b2
	    * @param defaultValue
	    * @return
	    */
	   public static <T extends Number> BigDecimal safeDivide(T b1, T b2, BigDecimal defaultValue) {
	       if (null == b1 || null == b2) {
	           return defaultValue;
	       }
	       try {
	           return BigDecimal.valueOf(b1.doubleValue()).divide(BigDecimal.valueOf(b2.doubleValue()), 2, BigDecimal.ROUND_HALF_UP);
	       } catch (Exception e) {
	           return defaultValue;
	       }
	   }
	 
	   /**
	    * BigDecimal的乘法运算封装
	    * @param b1
	    * @param b2
	    * @return
	    */
	   public static <T extends Number> BigDecimal safeMultiply(T b1, T b2) {
	       if (null == b1 || null == b2) {
	           return BigDecimal.ZERO;
	       }
	       return BigDecimal.valueOf(b1.doubleValue()).multiply(BigDecimal.valueOf(b2.doubleValue())).setScale(2, BigDecimal.ROUND_HALF_UP);
	   }

	   /**
	    * 
	    * @param args
	    */
	   public static void main(String[] args) {
	      // System.out.println(divide(1, 8));
	      // System.out.println(format(multiply(3.55, 2.44),2));
	      // System.out.println(divide(1.0, 3.0));
	      // System.out.println(add(2.79, -3.0));
	      System.out.println(doubleTrans(10000.0));
	   }


	}

