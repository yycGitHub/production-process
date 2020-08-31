
//综合部追溯条码打印（打印机型号：Brade GS25T  标签大小 30*50 两个并排 一次需要传入两个条码）
//src:二维码追溯网址
//src1:二维码追溯网址
function dayinzichan(name, model, barcode, name1, model1, barcode1) {
    if (name != "" && model != "" && barcode != "") {
        var TSCObj;
        TSCObj = new ActiveXObject("TSCActiveX.TSCLIB"); //条码打印对象
        // 	打印机名字
        TSCObj.ActiveXopenport("Brade GS25T");
        // 	设定标签的宽度、高度、打印速度、打印浓度、传感器类别、gap/black mark垂直间距、gap/black mark 偏移距离
        TSCObj.ActiveXsetup("102", "30", "5", "8", "0", "4", "0");
        // 	清除
        TSCObj.ActiveXclearbuffer();
        TSCObj.ActiveXsendcommand("DIRECTION 1");
        TSCObj.ActiveXwindowsfont(756, 170, 25, 180, 2, 0, "黑体", "名称:" + name);
        //	文字X方向起始点、文字Y 方向起始点、字体高度、旋转角度、字体外形(0：标准、1：斜体、2：粗体、3：粗斜体)、底线(0：无1：有)、字体名称、打印文字内容
        TSCObj.ActiveXwindowsfont(756, 135, 25, 180, 2, 0, "黑体", "类型:" + model);
        TSCObj.ActiveXwindowsfont(756, 100, 25, 180, 2, 0, "黑体", "条码:" + barcode);
        TSCObj.ActiveXbarcode("506", "20", "128", "45", "0", "0", "2", "2", barcode);
        if (name1 != "" && model1 != "" && barcode1 != "") {
            TSCObj.ActiveXwindowsfont(340, 170, 25, 180, 2, 0, "黑体", "名称:" + name1);
            //	文字X方向起始点、文字Y 方向起始点、字体高度、旋转角度、字体外形(0：标准、1：斜体、2：粗体、3：粗斜体)、底线(0：无1：有)、字体名称、打印文字内容
            TSCObj.ActiveXwindowsfont(340, 135, 25, 180, 2, 0, "黑体", "类型:" + model1);
            TSCObj.ActiveXwindowsfont(340, 100, 25, 180, 2, 0, "黑体", "条码:" + barcode1);
            TSCObj.ActiveXbarcode("90", "20", "128", "45", "0", "0", "2", "2", barcode1);
        }
        // 	设定打印标签个数、设定打印标签份数		
        TSCObj.ActiveXprintlabel("1", "1"); //打印标签内容
        //关闭指定的计算机端输出端口
        TSCObj.ActiveXcloseport();
    }
}

function dayinIE(src, fontname1, fontname2, fontname3, fontname4, fontname5, fontname6) {
    if (src != "" && fontname1 != "" && fontname2 != "" && fontname3 != "" && fontname4 != "" && fontname5 != "" && fontname6 != "") {
        var printData = new Object();
        var printer = new Object();
        printer.printerType = "TSC TTP-346MU";
        printer.isMiddlePlace = false;
        printer.maximumPrintSpeed = 10;
        printer.resolvingPower = "300dpi";
        printer.pixel = 11.81;
        printData.printer = printer;
        var labelSpecification = new Object();
        labelSpecification.labelWidth = 80;
        labelSpecification.labelHeight = 40;
        labelSpecification.labelNumber = 1;
        labelSpecification.labelLeftmargin = 1.5;
        labelSpecification.labelHorizontalInterval = 0;
        labelSpecification.labelVerticalInterval = 2;
        labelSpecification.materialName = "亮白PET";
        labelSpecification.maximumPrintSpeed = 4;
        labelSpecification.printingConcentration = 15;
        printData.labelSpecification = labelSpecification;
        var labelContentList = new Array();
        var labelContent = new Object();
        var dataList = new Array();

        var qRCodeData = new Object();
        qRCodeData.xStartPoint = 6.12;
        qRCodeData.yStartPoint = 8.31+2;
        qRCodeData.eCCLevel = "L";
        qRCodeData.cellWidth = 6;
        qRCodeData.mode = "A";
        qRCodeData.rotation = 0;
        qRCodeData.model = "M2";
        qRCodeData.mask = "S3";
        qRCodeData.qRCodeData = src;
        var labelData1 = new Object();
        labelData1.dataType = "QRCode";
        labelData1.dataJson = qRCodeData;
        dataList.push(labelData1);
        
 		var fontData1 = new Object();
        fontData1.xStartPoint = 8.62;
        fontData1.yStartPoint = 26.50+2;
        fontData1.fontHeight = 2.54;
        fontData1.rotation = 0;
        fontData1.fontShape = 2;
        fontData1.underline = 0;
        fontData1.fontName = "黑体";
        fontData1.textContent = "湖南12396";
        var labelData2 = new Object();
        labelData2.dataType = "Font";
        labelData2.dataJson = fontData1;
        dataList.push(labelData2);

        var fontData2 = new Object();
        fontData2.xStartPoint = 6.58;
        fontData2.yStartPoint = 30.00+2;
        fontData2.fontHeight = 2.54;
        fontData2.rotation = 0;
        fontData2.fontShape = 2;
        fontData2.underline = 0;
        fontData2.fontName = "黑体";
        fontData2.textContent = "科技扶贫兴农";
        var labelData3 = new Object();
        labelData3.dataType = "Font";
        labelData3.dataJson = fontData2;
        dataList.push(labelData3);
        
        var fontData3 = new Object();
        fontData3.xStartPoint = 25.18;
        fontData3.yStartPoint = 9.34+2;
        fontData3.fontHeight = 2.54;
        fontData3.rotation = 0;
        fontData3.fontShape = 2;
        fontData3.underline = 0;
        fontData3.fontName = "黑体";
        fontData3.textContent = fontname1;
        var labelData4 = new Object();
        labelData4.dataType = "Font";
        labelData4.dataJson = fontData3;
        dataList.push(labelData4);

        var fontData4 = new Object();
        fontData4.xStartPoint = 25.18;
        fontData4.yStartPoint = 13.15+2;
        fontData4.fontHeight = 2.54;
        fontData4.rotation = 0;
        fontData4.fontShape = 2;
        fontData4.underline = 0;
        fontData4.fontName = "黑体";
        fontData4.textContent = fontname2;
        var labelData5 = new Object();
        labelData5.dataType = "Font";
        labelData5.dataJson = fontData4;
        dataList.push(labelData5);

        var fontData5 = new Object();
        fontData5.xStartPoint = 25.18;
        fontData5.yStartPoint = 16.96+2;
        fontData5.fontHeight = 2.54;
        fontData5.rotation = 0;
        fontData5.fontShape = 2;
        fontData5.underline = 0;
        fontData5.fontName = "黑体";
        fontData5.textContent = fontname3;
        var labelData6 = new Object();
        labelData6.dataType = "Font";
        labelData6.dataJson = fontData5;
        dataList.push(labelData6);

        var fontData6 = new Object();
        fontData6.xStartPoint = 25.18;
        fontData6.yStartPoint = 20.77+2;
        fontData6.fontHeight = 2.54;
        fontData6.rotation = 0;
        fontData6.fontShape = 2;
        fontData6.underline = 0;
        fontData6.fontName = "黑体";
        fontData6.textContent = fontname4;
        var labelData7 = new Object();
        labelData7.dataType = "Font";
        labelData7.dataJson = fontData6;
        dataList.push(labelData7);

        var fontData7 = new Object();
        fontData7.xStartPoint = 25.18;
        fontData7.yStartPoint = 24.58+2;
        fontData7.fontHeight = 2.54;
        fontData7.rotation = 0;
        fontData7.fontShape = 2;
        fontData7.underline = 0;
        fontData7.fontName = "黑体";
        fontData7.textContent = fontname5;
        var labelData8 = new Object();
        labelData8.dataType = "Font";
        labelData8.dataJson = fontData7;
        dataList.push(labelData8);

        var fontData8 = new Object();
        fontData8.xStartPoint = 25.18;
        fontData8.yStartPoint = 28.39+2;
        fontData8.fontHeight = 2.54;
        fontData8.rotation = 0;
        fontData8.fontShape = 2;
        fontData8.underline = 0;
        fontData8.fontName = "黑体";
        fontData8.textContent = fontname6;
        var labelData9 = new Object();
        labelData9.dataType = "Font";
        labelData9.dataJson = fontData8;
        dataList.push(labelData9);

        labelContent.dataList = dataList;
        labelContentList.push(labelContent);
        printData.labelContentList = labelContentList;
        var json = JSON.stringify(printData);
        labelPrinting(printData);

    }
}

//通用打印 printData示例 {"printer":{"printerType":"TSC TTP-346MU","isMiddlePlace":false,"maximumPrintSpeed":10,"resolvingPower":"300dpi","pixel":11.81},"labelSpecification":{"labelWidth":80,"labelHeight":120,"labelNumber":1,"labelLeftmargin":2,"labelHorizontalInterval":0,"labelVerticalInterval":2,"materialName":"亮白PET","maximumPrintSpeed":4,"printingConcentration":15},"labelContentList":[{"dataList":[{"dataType":"Font","dataJson":{"xStartPoint":3.93,"yStartPoint":24.56,"fontHeight":3.9,"rotation":0,"fontShape":0,"underline":0,"fontName":"方正兰亭粗黑_GBK","textContent":"品种/砧木:"}},{"dataType":"Font","dataJson":{"xStartPoint":20.86,"yStartPoint":24.81,"fontHeight":3.39,"rotation":0,"fontShape":2,"underline":0,"fontName":"方正兰亭黑_GBK","textContent":"橘湘早/枳"}},{"dataType":"Font","dataJson":{"xStartPoint":3.93,"yStartPoint":34.72,"fontHeight":3.9,"rotation":0,"fontShape":0,"underline":0,"fontName":"方正兰亭粗黑_GBK","textContent":"生产企业:"}},{"dataType":"Font","dataJson":{"xStartPoint":19.59,"yStartPoint":34.97,"fontHeight":3.39,"rotation":0,"fontShape":2,"underline":0,"fontName":"方正兰亭黑_GBK","textContent":"湖南省安宁农业科技有限公司"}},{"dataType":"Font","dataJson":{"xStartPoint":3.93,"yStartPoint":44.88,"fontHeight":3.9,"rotation":0,"fontShape":0,"underline":0,"fontName":"方正兰亭粗黑_GBK","textContent":"生产地址:"}},{"dataType":"Font","dataJson":{"xStartPoint":19.59,"yStartPoint":45.13,"fontHeight":3.39,"rotation":0,"fontShape":2,"underline":0,"fontName":"方正兰亭黑_GBK","textContent":"宁乡市回龙铺镇天鹅村良种基地"}},{"dataType":"Font","dataJson":{"xStartPoint":3.93,"yStartPoint":55.04,"fontHeight":3.9,"rotation":0,"fontShape":0,"underline":0,"fontName":"方正兰亭粗黑_GBK","textContent":"推荐种植密度:"}},{"dataType":"Font","dataJson":{"xStartPoint":28.37,"yStartPoint":55.29,"fontHeight":3.39,"rotation":0,"fontShape":2,"underline":0,"fontName":"方正兰亭黑_GBK","textContent":"60～70株/亩"}},{"dataType":"Font","dataJson":{"xStartPoint":3.93,"yStartPoint":65.2,"fontHeight":3.9,"rotation":0,"fontShape":0,"underline":0,"fontName":"方正兰亭粗黑_GBK","textContent":"联系方式:"}},{"dataType":"Font","dataJson":{"xStartPoint":19.59,"yStartPoint":65.45,"fontHeight":3.39,"rotation":0,"fontShape":2,"underline":0,"fontName":"方正兰亭黑_GBK","textContent":"13873706065"}},{"dataType":"Font","dataJson":{"xStartPoint":3.93,"yStartPoint":75.36,"fontHeight":3.9,"rotation":0,"fontShape":0,"underline":0,"fontName":"方正兰亭粗黑_GBK","textContent":"溯源码:"}},{"dataType":"Font","dataJson":{"xStartPoint":16.2,"yStartPoint":75.61,"fontHeight":3.39,"rotation":0,"fontShape":2,"underline":0,"fontName":"方正兰亭黑_GBK","textContent":"YYB20180906112509703640"}},{"dataType":"Font","dataJson":{"xStartPoint":3.93,"yStartPoint":85.52,"fontHeight":3.9,"rotation":0,"fontShape":0,"underline":0,"fontName":"方正兰亭粗黑_GBK","textContent":"种子经营许可证:"}},{"dataType":"Font","dataJson":{"xStartPoint":29.75,"yStartPoint":85.77,"fontHeight":3.39,"rotation":0,"fontShape":2,"underline":0,"fontName":"方正兰亭黑_GBK","textContent":"D(湘益安)农种许字(2017)第001号"}},{"dataType":"Font","dataJson":{"xStartPoint":3.93,"yStartPoint":95.68,"fontHeight":3.9,"rotation":0,"fontShape":0,"underline":0,"fontName":"方正兰亭粗黑_GBK","textContent":"技术支持:"}},{"dataType":"Font","dataJson":{"xStartPoint":19.59,"yStartPoint":95.94,"fontHeight":3.39,"rotation":0,"fontShape":2,"underline":0,"fontName":"方正兰亭黑_GBK","textContent":"湖南农业大学"}},{"dataType":"Font","dataJson":{"xStartPoint":19.59,"yStartPoint":101.02,"fontHeight":3.39,"rotation":0,"fontShape":2,"underline":0,"fontName":"方正兰亭黑_GBK","textContent":"湖南柑橘脱毒中心"}},{"dataType":"QRCode","dataJson":{"xStartPoint":54.73,"yStartPoint":94.14,"eCCLevel":"M","cellWidth":7,"mode":"A","rotation":0,"model":"M2","mask":"S3","qRCodeData":"http://trace.vip/YYB20180906112509703640"}}]}]}
function labelPrinting(printData) {
    if (printData != "" && printData != undefined) {
        debugger;
        if (printData.labelContentList.length <= printData.labelSpecification.labelNumber) {
            var TSCObj;
            TSCObj = new ActiveXObject("TSCActiveX.TSCLIB"); //条码打印对象
            // 	打印机型号（打印机名称）
            TSCObj.ActiveXopenport(printData.printer.printerType);
            //TSCObj.ActiveXsetup("打印宽度","打印高度","打印速度","打印浓度（0-15）","感应器类别字串型，0 表示使用垂直間距感測器(gap sensor)， 1 表示使用黑標感測器(black mark senso)","Gap/Black mark垂直间距(mm)","Gap/Black mark偏移距离(mm)");
            var printWidth = printData.labelSpecification.labelWidth * printData.labelSpecification.labelNumber + printData.labelSpecification.labelHorizontalInterval * (printData.labelSpecification.labelNumber - 1);
            var printHeight = printData.labelSpecification.labelHeight;
            var printSpeed = printData.labelSpecification.maximumPrintSpeed;
            if (!printData.printer.isMiddlePlace) {
                printWidth += printData.labelSpecification.labelLeftmargin
            }
            if (printData.labelSpecification.maximumPrintSpeed > printData.printer.maximumPrintSpeed) {
                printSpeed = printData.printer.maximumPrintSpeed
            }
            TSCObj.ActiveXsetup("\"" + printWidth + "\"", "\"" + printHeight + "\"", "\"" + printSpeed + "\"", "\"" + printData.labelSpecification.printingConcentration + "\"", "0", "\"" + printData.labelSpecification.labelVerticalInterval + "\"", "0");
            TSCObj.ActiveXsendcommand("DIRECTION 1");//设置标签方向 ,DIRECTION 1 左上角 (x,y)={0,0};DIRECTION 0 右下角 (x,y)={0,0};
            TSCObj.ActiveXsendcommand("SET TEAR ON");
            // 	清除
            TSCObj.ActiveXclearbuffer();
            /*  
            1、ActiveXprinterfont采用机器内置编码通常用来打英文。  
            2、ActiveXwindowsfont可以输出汉字，但是必须是系统中存在的字体。
            3、ActiveXbarcode 绘制BARCODE条码。
            4、ActiveXsendcommand 绘制QRCODE二維  
      
            ActiveXprinterfont ("a","b","c","d","e","f","g");  
            a：字符串，文字X方向起始点，以点表示。  
            b：字符串，文字Y方向起始点，以点表示。  
            c：內建字型名称，共12种（1: 8*12 dots 2: 12*20 dots 3: 16*24 dots 4: 24*32 dots 5: 32*48 dots TST24.BF2: 繁體中文 24*24 TST16.BF2: 繁體中文 16*16 TTT24.BF2: 繁體中文 24*24 (電信碼) TSS24.BF2: 簡體中文 24*24 TSS16.BF2: 簡體中文 16*16 K: 韓文 24*24 L: 韓文 16*16 ）  
            d：字符串，旋转角度  
            e：字符串，X方向放大倍率1-8  
            f：字符串，Y方向放大倍率1-8  
            g：字符串，打印内容  
      
            ActiveXwindowsfont(a,b,c,d,e,f,g,h)  
            说明：使用Windows TTF字体打印文字。  
            参数：  
            a：整数类型，文字X方向起始点，以点表示。  
            b：整数类型，文字Y方向起始点，以点表示。  
            c：整数类型，字体高度，以点表示。  
            d：整数类型，旋转角度，逆时针方向旋转。0-旋转0°，90-旋转90°，180-旋转180°，270-旋转270°。  
            e：整数类型，字体外形。0：标准；1：斜体；2：粗体；3：粗斜体。  
            f：整数类型，下划线，0：无下划线；1：加下划线。  
            g：字符串类型，字体名称。如：Arial，Times new Roman。  
            h：字符串类型，打印文字内容。  

            ActiveXbarcode(X, Y, CodeType， Height, Readable, Rotation, Narrow,  Wide, Code);
            功能：绘制BARCODE条码         
            参数说明
            X BARCODE条码左上角X坐标
            Y BARCODE条码左上角Y坐标
            CodeType 条码方式 128 128M 25 25C 39 39C 93 EAN13  EAN13+2 EAN13+5 EAN8 EAN8+2 EAN8+5...
            Height 条码高度
            Readable 打印条码的同时，是否打印条码的文本信息 0 不打印 1 打印
            Rotation 顺时针旋转角度
            Narrow 条码 宽  比例因子
            Wide 条码 窄  比例因子
            Code 条码内容
            TSCObj.ActiveXbarcode("506", "20", "128", "45", "0", "0", "2", "2", barcode);

            ActiveXsendcommand(QRCODE X, Y, ECC Level, cell width, mode, rotation, model, mask,"Data string”)
            功能：绘制QRCODE二維条码        
            参数说明
            X QRCODE条码左上角X坐标
            Y QRCODE条码左上角Y坐标
            ECC level 错误纠正能力等級
            L 7%
            M 15%
            Q 25%
            H 30%
            cell width    1~10
            mode  自动生成编码/手动生成编码
            A Auto
            M Manual
            rotation  顺时针旋转角度
            0 不旋转
            90    顺时针旋转90度
            180   顺时针旋转180度
            270   顺时针旋转270度
            model 条码生成样式
            1 (预设), 原始版本
            2 扩大版本
            mask  范围：0~8，预设7
            Data string   条码资料內容
            */

            for (var i = 0; i < printData.labelContentList.length; i++) {
                var labelX = (printData.labelSpecification.labelWidth + printData.labelSpecification.labelHorizontalInterval) * i
                if (!printData.printer.isMiddlePlace) {
                    labelX += printData.labelSpecification.labelLeftmargin
                }
                for (var j = 0; j < printData.labelContentList[i].dataList.length; j++) {
                    switch (printData.labelContentList[i].dataList[j].dataType) {
                        case "Font":
                            TSCObj.ActiveXwindowsfont(Number((labelX + printData.labelContentList[i].dataList[j].dataJson.xStartPoint) * printData.printer.pixel),
                                Number(printData.labelContentList[i].dataList[j].dataJson.yStartPoint * printData.printer.pixel),
                                Number(printData.labelContentList[i].dataList[j].dataJson.fontHeight * printData.printer.pixel),
                                printData.labelContentList[i].dataList[j].dataJson.rotation,
                                printData.labelContentList[i].dataList[j].dataJson.fontShape,
                                printData.labelContentList[i].dataList[j].dataJson.underline,
                                printData.labelContentList[i].dataList[j].dataJson.fontName,
                                printData.labelContentList[i].dataList[j].dataJson.textContent);
                            break;
                        case "QRCode":
                            TSCObj.ActiveXsendcommand("QRCODE " + Number((labelX + printData.labelContentList[i].dataList[j].dataJson.xStartPoint) * printData.printer.pixel)
                                + "," + Number(printData.labelContentList[i].dataList[j].dataJson.yStartPoint * printData.printer.pixel)
                                + "," + printData.labelContentList[i].dataList[j].dataJson.eCCLevel
                                + "," + Number(printData.labelContentList[i].dataList[j].dataJson.cellWidth)
                                + "," + printData.labelContentList[i].dataList[j].dataJson.mode
                                + "," + printData.labelContentList[i].dataList[j].dataJson.rotation
                                + "," + printData.labelContentList[i].dataList[j].dataJson.model
                                + "," + printData.labelContentList[i].dataList[j].dataJson.mask
                                + ",\"" + printData.labelContentList[i].dataList[j].dataJson.qRCodeData + "\"");
                            break;
                        case "Barcode":
                            TSCObj.ActiveXbarcode("\"" + Number((labelX + printData.labelContentList[i].dataList[j].dataJson.xStartPoint) * printData.printer.pixel) + "\"",
                                "\"" + Number(printData.labelContentList[i].dataList[j].dataJson.yStartPoint * printData.printer.pixel) + "\"",
                                "\"" + printData.labelContentList[i].dataList[j].dataJson.codeType + "\"",
                                "\"" + Number(printData.labelContentList[i].dataList[j].dataJson.height * printData.printer.pixel) + "\"",
                                "\"" + printData.labelContentList[i].dataList[j].dataJson.readable + "\"",
                                "\"" + printData.labelContentList[i].dataList[j].dataJson.rotation + "\"",
                                "\"" + printData.labelContentList[i].dataList[j].dataJson.narrow + "\"",
                                "\"" + printData.labelContentList[i].dataList[j].dataJson.wide + "\"",
                                printData.labelContentList[i].dataList[j].dataJson.code);
                            break;
                    }
                }
            }

            // 	设定打印标签个数、设定打印标签份数		
            TSCObj.ActiveXprintlabel("1", "1"); //打印标签内容
            //关闭指定的计算机端输出端口
            TSCObj.ActiveXcloseport();
        }
    }
}