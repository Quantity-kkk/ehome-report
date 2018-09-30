package report.java.jrreport.util;

import java.io.PrintStream;
import java.math.BigDecimal;

public class JRMoneyFormat
{
  private static final String[] CN_UPPER_NUMBER = { "零", "壹", "贰", "叁", "肆", 
    "伍", "陆", "柒", "捌", "玖" };

  private static final String[] CN_UPPER_MONETRAY_UNIT = { "分", "角", "元", 
    "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "兆", "拾", 
    "佰", "仟" };
  private static final String CN_FULL = "整";
  private static final String CN_NEGATIVE = "负";
  private static final int MONEY_PRECISION = 2;
  private static final String CN_ZEOR_FULL = "零元整";

  public static String number2CNMontrayUnit(BigDecimal numberOfMoney)
  {
    StringBuffer sb = new StringBuffer();

    int signum = numberOfMoney.signum();

    if (signum == 0) {
      return "零元整";
    }

    long number = numberOfMoney.movePointRight(2)
      .setScale(0, 4).abs().longValue();

    long scale = number % 100L;
    int numUnit = 0;
    int numIndex = 0;
    boolean getZero = false;

    if (scale <= 0L) {
      numIndex = 2;
      number /= 100L;
      getZero = true;
    }
    if ((scale > 0L) && (scale % 10L <= 0L)) {
      numIndex = 1;
      number /= 10L;
      getZero = true;
    }
    int zeroSize = 0;

    while (number > 0L)
    {
      numUnit = (int)(number % 10L);
      if (numUnit > 0) {
        if ((numIndex == 9) && (zeroSize >= 3)) {
          sb.insert(0, CN_UPPER_MONETRAY_UNIT[6]);
        }
        if ((numIndex == 13) && (zeroSize >= 3)) {
          sb.insert(0, CN_UPPER_MONETRAY_UNIT[10]);
        }
        sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
        sb.insert(0, CN_UPPER_NUMBER[numUnit]);
        getZero = false;
        zeroSize = 0;
      } else {
        zeroSize++;
        if (!getZero) {
          sb.insert(0, CN_UPPER_NUMBER[numUnit]);
        }
        if (numIndex == 2) {
          if (number > 0L)
            sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
        }
        else if (((numIndex - 2) % 4 == 0) && (number % 1000L > 0L)) {
          sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
        }
        getZero = true;
      }

      number /= 10L;
      numIndex++;
    }

    if (signum == -1) {
      sb.insert(0, "负");
    }

    if (scale <= 0L) {
      sb.append("整");
    }
    return sb.toString();
  }

  public static void main(String[] args) {
    double money = 2020004.01D;
    BigDecimal numberOfMoney = new BigDecimal(money);
    String s = number2CNMontrayUnit(numberOfMoney);
    System.out.println("你输入的金额为：【" + money + "】   #--# [" + s.toString() + "]");
  }

  public String format(String str, boolean flag) {
    BigDecimal numberOfMoney = new BigDecimal(str);
    String s = number2CNMontrayUnit(numberOfMoney);
    return s;
  }

  public String transition(String si) {
    return format(si, true).replaceAll("壹", "一")
      .replaceAll("贰", "二")
      .replaceAll("叁", "三")
      .replaceAll("肆", "四")
      .replaceAll("伍", "五")
      .replaceAll("陆", "六")
      .replaceAll("柒", "七")
      .replaceAll("捌", "八")
      .replaceAll("玖", "九")
      .replaceAll("拾", "十")
      .replaceAll("佰", "百")
      .replaceAll("仟", "千")
      .replaceAll("整", "");
  }

  public String formatM(String str) {
    int dotPoint = str.indexOf(".");
    if (dotPoint != -1) {
      str = str.substring(0, str.indexOf("."));
    }
    while (str.startsWith("0")) {
      str = str.substring(1);
    }
    str = str.replaceAll("0", "零")
      .replaceAll("1", "一")
      .replaceAll("2", "二")
      .replaceAll("3", "三")
      .replaceAll("4", "四")
      .replaceAll("5", "五")
      .replaceAll("6", "六")
      .replaceAll("7", "七")
      .replaceAll("8", "八")
      .replaceAll("9", "九");
    return str;
  }
}

/*
 * Qualified Name:     report.java.jrreport.util.JRMoneyFormat
*
 */