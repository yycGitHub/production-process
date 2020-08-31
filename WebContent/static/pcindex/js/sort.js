function initials(pro) {//公众号排序
    //        var SortList = $(".sort_list");
    //        var SortBox = $(".sort_box");
    //        SortList.sort(asc_sort).appendTo('.sort_box'); //按首字母排序
    //        function asc_sort(a, b) {
    //            return makePy($(b).find('.num_name').text().charAt(0))[0].toUpperCase() < makePy($(a).find('.num_name').text().charAt(0))[0].toUpperCase() ? 1 : -1;
    //        }

    //        var SortList = $(".sort_list .select-items li");
    //        var SortBox = $(".sort_list .select-items ul");


    var SortList = $(pro).find("option");
    var SortBox = $(pro);

    SortList.sort(asc_sort).appendTo($(pro)); //按首字母排序

    function asc_sort(a, b) {
        return makePy($(b).html().charAt(0))[0].toUpperCase() < makePy($(a).html().charAt(0))[0].toUpperCase() ? 1 : -1;
    }
    
    var initials = [];
    var num = 0;
    SortList.each(function (i) {
        var initial = makePy($(this).html().charAt(0))[0].toUpperCase();
        var initial2 = $(this).html().substring(0, 4);
        if (initial >= 'A' && initial <= 'Z') {
            if (initials.indexOf(initial) === -1)
                initials.push(initial);
        }
        else {
            num++;
        }

    });

    $.each(initials, function (index, value) {//添加首字母标签
        // SortBox.append('<div class="sort_letter" id="' + value + '">' + value + '</div>');
        SortBox.append('<optgroup disabled="disabled" style="font-weight :bold;"  label="' + value + '"  id="' + value + '"></optgroup>');
    });
    if (num != 0) { SortBox.append('<optgroup disabled="disabled" id="default">#</optgroup>'); }

    for (var i = 0; i < SortList.length; i++) {//插入到对应的首字母后面
        
        var letter = makePy(SortList.eq(i).html().charAt(0))[0].toUpperCase();
        var letter2 = SortList.eq(i).html().substring(0, 4);
        switch (letter) {
            case "A":
                $('#A').after(SortList.eq(i));
                break;
            case "B":
                $('#B').after(SortList.eq(i));
                break;
            case "C":
                $('#C').after(SortList.eq(i));
                break;
            case "D":
                $('#D').after(SortList.eq(i));
                break;
            case "E":
                $('#E').after(SortList.eq(i));
                break;
            case "F":
                $('#F').after(SortList.eq(i));
                break;
            case "G":
                $('#G').after(SortList.eq(i));
                break;
            case "H":
                $('#H').after(SortList.eq(i));
                break;
            case "I":
                $('#I').after(SortList.eq(i));
                break;
            case "J":
                $('#J').after(SortList.eq(i));
                break;
            case "K":
                $('#K').after(SortList.eq(i));
                break;
            case "L":
                $('#L').after(SortList.eq(i));
                break;
            case "M":
                $('#M').after(SortList.eq(i));
                break;
            case "N":
                $('#N').after(SortList.eq(i));
                break;
            case "O":
                $('#O').after(SortList.eq(i));
                break;
            case "P":
                $('#P').after(SortList.eq(i));
                break;
            case "Q":
                $('#Q').after(SortList.eq(i));
                break;
            case "R":
                $('#R').after(SortList.eq(i));
                break;
            case "S":
                $('#S').after(SortList.eq(i));
                break;
            case "T":
                $('#T').after(SortList.eq(i));
                break;
            case "U":
                $('#U').after(SortList.eq(i));
                break;
            case "V":
                $('#V').after(SortList.eq(i));
                break;
            case "W":
                $('#W').after(SortList.eq(i));
                
                break;
            case "X":
                $('#X').after(SortList.eq(i));
                break;
            case "Y":
                $('#Y').after(SortList.eq(i));
                break;
            case "Z":
                $('#Z').after(SortList.eq(i));
                break;
            default:
                switch (letter2) {
                    default:
                        $('#default').after(SortList.eq(i));
                }
                break;
        }
    };
}