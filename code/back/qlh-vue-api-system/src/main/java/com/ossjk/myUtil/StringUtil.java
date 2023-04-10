package com.ossjk.myUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class StringUtil {

    public static final SimpleDateFormat smt1;
    public static final SimpleDateFormat smt2;
    public static final SimpleDateFormat smt3;
    public static final SimpleDateFormat smt4;
    public static final SimpleDateFormat isoMthFormat ;

    public static final SimpleDateFormat smt8;


    public static final Map<String, String> hanMapper;

    static {
        smt1 = new SimpleDateFormat("yyyy.MM.dd");
        smt2 = new SimpleDateFormat("yyyy-MM-dd");
        smt3 = new SimpleDateFormat("yyyy-MM");
        smt4 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        smt8 = new SimpleDateFormat("yyyyMM-dd_HHmmss");
        isoMthFormat = new SimpleDateFormat("yyyy-MM");

        hanMapper = new HashMap();
        //异型
        String s1[] = {"⺎","⺒","⺓","⺟","⺠","龍","阮","劉","柳","流","溜","琉","留","硫","六","陸","倫","崙","淪","輪","律","栗","率","隆","利","吏","履","易","李","梨","泥","理","裏","裡","里","藺","鱗","麟","林","立","笠","粒","狀","炙","什","茶","刺","切","度","拓","洞","行","降","見","廓","兀","益","礼","神","祥","福","靖","精","羽","逸","都","館","侮","僧","免","勉","勤","嘆","器","墨","層","懲","敏","暑","梅","漢","煮","琢","碑","社","祉","祈","祐","祖","祝","禎","突","繁","署","者","著","褐","視","謹","賓","贈","逸","頻","𤋮","⽯","⽰","⽲","⽴","⽵","⽶","⽷","⽸","⽹","⽺","⽻","⽼","⽽","⽿","⾀","⾂","⾃","⾄","⾇","⾈","⾉","⾊","⾍","⾎","⾏","⾐","⾒","⾓","⾔","⾕","⾖","⾙","⾚","⾛","⾜","⾝","⾞","⾟","⾠","⾢","⾣","⾤","⾥","⾦","⾧","⾨","⾩","⾪","⾬","⾭","⾮","⾯","⾰","⾱","⾳","⾴","⾵","⾶","⾷","⾸","⾹","⾺","⾻","⾼","⿂","⿅","⿆","⿇","⿈","⿉","⿊","⿎","⿏","⿐","⿓","开","四","回","囟","因","团","园","困","固","国","图","囿","圃","圆","囵","围","园","圆","圔","图","团","豈","更","車","賈","串","句","契","金","奈","羅","蘿","洛","烙","珞","落","酪","蘭","嵐","藍","拉","廊","朗","浪","狼","郎","來","冷","勞","老","路","露","魯","鷺","錄","鹿","弄","籠","磊","雷","勒","凌","稜","綾","菱","拏","諾","丹","寧","率","異","北","磻","便","復","不","省","葉","辰","沈","拾","若","略","亮","兩","凉","梁","良","量","呂","女","旅","閭","麗","黎","力","年","蓮","連","列","烈","裂","廉","念","令","怜","玲","瑩","羚","聆","零","例","禮","了","僚","寮","料","樂","⺸","⺼","⻁","⻄","⻅","⻆","⻉","⻋","⻑","⻓","⻔","⻘","⻙","⻚","⻛","⻜","⻝","⻢","⻥","⻦","⻨","⻩","⻬","⻰","⼀","⼄","⼆","⼈","⼉","⼊","⼋","⼏","⼑","⼒","⼗","⼘","⼜","⼝","⼞","⼟","⼠","⼤","⼥","⼦","⼨","⼩","⼭","⼯","⼰","⼱","⼲","⼴","⼷","⼸","⼼","⼽","⼾","⼿","⽀","⽂","⽃","⽄","⽅","⽆","⽇","⽈","⽉","⽊","⽌","⽐","⽑","⽒","⽓","⽔","⽕","⽗","⽘","⽚","⽛","⽜","⽝","⽞","⽟","⽠","⽡","⽢","⽣","⽤","⽥","⽩","⽪","⽬","⽭","⽮"};
        //半角中文字符
        String s2[] = {"兀","已","幺","母","民","龍","阮","劉","柳","流","溜","琉","留","硫","六","陸","倫","崙","淪","輪","律","栗","率","隆","利","吏","履","易","李","梨","泥","理","裹","裡","里","蔺","鳞","麟","林","立","笠","粒","壯","炙","什","茶","刺","切","度","拓","洞","行","降","見","廊","兀","盒","礼","神","祥","福","靖","精","羽","逸","都","館","侮","僧","免","勉","勤","嘆","器","墨","層","懲","敏","暑","梅","漢","煮","琢","碑","社","祉","祈","祐","祖","祝","祯","突","繁","署","者","著","褐","視","謹","宝","赠","逸","頻","熙","石","示","禾","立","竹","米","系","缶","网","羊","羽","老","而","耳","聿","臣","自","至","舛","舟","艮","色","虫","血","行","衣","見","角","言","谷","豆","貝","赤","走","足","身","車","辛","辰","邑","酉","采","里","金","長","門","阜","隶","雨","靑","非","面","革","韋","音","頁","風","飛","食","首","香","馬","骨","高","魚","鹿","麥","麻","黄","黍","黒","鼓","鼠","鼻","龍","开","四","会","卤","因","团","园","困","固","国","图","囿","圃","圆","囵","围","园","圆","阖","图","团","豈","更","車","贾","串","句","契","金","奈","羅","蘿","洛","烙","珞","落","酪","蘭","岚","藍","拉","廊","郎","浪","狼","郎","来","冷","劳","老","路","露","鲁","鹭","録","鹿","弄","龍","磊","雷","勒","凌","稜","绫","菱","拏","诺","丹","寜","率","異","北","磻","便","复","不","省","叶","辰","沈","拾","若","略","亮","两","凉","梁","良","量","吕","女","旅","闾","麓","黎","力","年","莲","连","列","烈","裂","廉","念","令","怜","玲","莹","羚","聆","零","例","礼","了","僚","寮","料","乐","芈","月","虎","西","见","角","贝","车","长","长","门","青","韦","页","风","飞","食","马","鱼","鸟","麦","黄","齐","龙","一","乙","二","人","儿","入","八","几","刀","力","十","卜","又","口","口","土","士","大","女","子","寸","小","山","工","己","巾","干","广","弋","弓","心","戈","户","手","支","文","斗","斤","方","无","日","曰","月","木","止","比","毛","氏","气","水","火","父","爻","片","牙","牛","犬","玄","玉","瓜","瓦","甘","生","用","田","白","皮","目","矛","矢"};

        for (int i = 0; i < s1.length; i++) {
            hanMapper.put(s1[i], s2[i]);
        }
    }

    /**
     * 压缩多个文件，压缩后的所有文件在同一目录下
     *
     * @param zipFile 压缩后的文件名
     * @param files 需要压缩的文件列表
     * @throws IOException IO异常
     */
    public static void zipMultipleFiles( File zipFile, File... files) throws IOException {
        ZipOutputStream zipOutputStream = null;
        try {
            // 输出流
            zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));
            // 遍历每一个文件，进行输出
            for (File file : files) {
                zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
                FileInputStream fileInputStream = new FileInputStream(file);
                int readLen;
                byte[] buffer = new byte[1024];
                while ((readLen = fileInputStream.read(buffer)) != -1) {
                    zipOutputStream.write(buffer, 0, readLen);
                }
                // 关闭流
                fileInputStream.close();
                zipOutputStream.closeEntry();
            }
        } finally {
            if (null != zipOutputStream) {
                try {
                    zipOutputStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    //根据给定的日期，计算周一和周日
    public  static Date[]  getMon_Sundays(String date   ) {
        try {

            Date[] reslut = new Date[2];
            java.util.Calendar cldr =  java.util.Calendar.getInstance();
            cldr.setTime(   smt2.parse(date)   );

            if(   cldr.get(  Calendar.DAY_OF_WEEK  )  !=  1  ){  // 不是周日
                cldr.add(    Calendar.DATE ,  (8 - cldr.get(  Calendar.DAY_OF_WEEK  ) )    );
            }
            cldr.set(Calendar.HOUR_OF_DAY,23);
            cldr.set(Calendar.MINUTE,59);
            cldr.set(Calendar.SECOND,59);

            reslut[1]=   cldr.getTime();

            cldr.set(Calendar.HOUR_OF_DAY,0);
            cldr.set(Calendar.MINUTE,0);
            cldr.set(Calendar.SECOND,0);
            cldr.add(    Calendar.DATE , -6  );
            reslut[0]=  cldr.getTime()    ;

            return reslut;
        }catch (Exception ex){
            return null;
        }
    }


    //计算上一月 或 下一月                                0:本月,-1:上月，1:下月
    public static String calMth(String mth,int datType) {
        //当前时间

        Calendar cld = java.util.Calendar.getInstance();
        try {
            Date nowDate = smt2.parse(mth+"-01");
            cld.setTime(nowDate);
            cld.add(Calendar.MONTH, datType);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  isoMthFormat.format( cld.getTime()   );
    }

    //处理异型字符
    public static String transXHan(String str)  {

        String[]  arrs = str.trim().replaceAll(" ","").split("");
        StringBuilder sb  = new StringBuilder();

        for (  String  s: arrs	) {
            if(   StringUtil.hanMapper.get(s) !=null){
                sb.append(StringUtil.hanMapper.get(s));
            }else{
                sb.append(s);
            }
        }

        return sb.toString();
    }


    public static String fmtDate(String dateStr) throws ParseException {
        return smt2.format(smt1.parse(dateStr));
    }

    // 全角转半角的 转换函数 去除空白、空格
    public static String all2half(String src) {
        if (src == null) {
            return src;
        }
        StringBuilder buf = new StringBuilder(src.length() + 1);
        char[] ca = src.toCharArray();
        for (int i = 0; i < src.length(); i++) {
            if (ca[i] >= 65281 && ca[i] <= 65374) { // 如果位于全角！到全角～区间内
                buf.append((char) (ca[i] - 65248));
            } else if (ca[i] == 12288) { // 如果是全角空格
                buf.append(' ');
            } else { // 不处理全角空格，全角！到全角～区间外的字符
                buf.append(ca[i]);
            }
        }
        return buf.toString().replaceAll(" ", "").trim();
    }

    public static String jsonToString(String json) {

        if (json == null) {
            return json;
        } else if (json.indexOf("[") >= 0) {
            return json.substring(1).replaceAll(" ", "").replaceAll("\"", "").replaceAll("]", "");
        } else if (json.indexOf(",") > 0) {
            return json.replaceAll(" ", "").replaceAll("\"", "");
        } else {
            return json;
        }

    }

    public static void main(String[] args) {
        Date  dates [] = StringUtil.getMon_Sundays(   "2021-11-25"   );

        System.out.println( dates [0] );
        System.out.println( dates [1] );
    }
}
