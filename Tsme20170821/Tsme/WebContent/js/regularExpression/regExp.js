var oneCharPattern = /[\u0000-\u2000]/;

var spacePattern=/^(\s)$/g;

var userNamePattern = /^(?!_)(?!.*?_$)[_a-zA-Z0-9\u4e00-\u9fa5]*$/;//用户名(汉字，英文字母，数字，下划线或其组合，不能以下划线开始或者结束)  

var IDPattern = /^(1[1-5]|2[1-3]|3[1-7]|4[1-6]|5[0-4]|6[1-5]|71|8[12])\d{4}(19\d{2}|200\d|201\d)((0[1-9])|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}(\d|X|x)$/;//身份证号

var phoneNumPattern = /^[1][358]\d([\-\.\s]?(\d{4})){2}$/;//手机号

//var emailPattern = /^(?!_)(?!.*?_$)[_a-zA-Z0-9]{6,18}@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2})$/;//邮箱

var emailPattern =/^(?!_)(?!.*?_$)([a-zA-Z0-9_\.\-]){2,18}\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;

var proNamePattern = /^[\u0000-\uffff]*$/;//产品名称

var subTitlePattern = /^[\u0000-\uffff]*$/;//副标题

var publishFirmPattern = /^[\u0000-\uffff]*$/;//出版社

/*var isoNumPattern = /^(ISBN|isbn)?[- ]?\d{3}[- ]?\d{3}[- ]?\d{4}[- ]?\d{2}[- ]?\d$/;//标准编号ISBN 978-962-3899-11-2（978-国家，语言或区位代码-出版社代码-书序码-校验码）
*/
var isoNumPattern = /^(ISBN|isbn)?[- ]?\d{3}[- ]?(\d{3}[- ]?(\d{4}[- ]?\d{2}[- ]?\d))|(\d{5}[- ]?\d[- ]?\d)|(\d[- ]?\d{3}[- ]\d{5}[- ]?\d)$/;

var chiClassPattern = /^([A-K]|[N]|[O-S]|T[^ACIORTW-Z]|DF)(\d{1,3})?[-]?(\d{1,3}([\.\/]?\d{1,3})?)?$|^\[([A-K]|[N]|[O-S]|T[^ACIORTW-Z]|DF)(\d{1,3})?[-]?(\d{1,3}([\.\/]?\d{1,3})?)?\]$|^\{([A-K]|[N]|[O-S]|T[^ACIORTW-Z]|DF)(\d{1,3})?[-]?(\d{1,3}([\.\/]?\d{1,3})?)?\}$/;//中图分类号

var copyRightPattern = /^[\u0000-\uffff]*$/;//版权信息

var previewPattern = /^[\u0000-\uffff]*$/;//产品简介

var authorPattern = /^[\u0000-\uffff]*$/;//作者

var rolePattern = /^[\u0000-\uffff]*$/;//角色

var birthPlacePattern = /^[a-zA-Z0-9\u4e00-\u9fa5]*$/;//出生地点

var homePagePattern =  /^(http|ftp|https):\/\/([\w\d\.\-]+(\:[\w\d\.&amp;%\$\-]+)*@)?(((2[0-4]\d|25[0-5]|[01]?\d\d?)\.){3}(2[0-4]\d|25[0-5]|[01]?\d\d?)|([\w\d\-]+\.)*[\w\d\-]+\.\w{2,4})(\:\d+)?([\w\d\.\,\?\'\/\+&amp;%\$#\=~_\-@]*)*$/;//个人主页

var researFieldPattern =/^[\u0000-\uffff]*$/;//研究领域

var resumePattern =/^[\u0000-\uffff]*$/;// 作者简介

var namePattern = /^[\u0000-\uffff]*$/;//作者名字

var lastnamePattern = /^[a-zA-Z\u4e00-\u9fa5]*$/;///^[\u4e00-\u9fa5]{1,20}$|^[\w]{1,30}$/

var firstnamePattern = /^[a-zA-Z\u4e00-\u9fa5]*$/;///^[\u4e00-\u9fa5]{1,40}$|^(\w{1,20}\s){0,4}\w{1,30}$/

var contentNamePattern = /^[\u0000-\uffff]*$/;//内容名称

var asinPattern = /^\d{6}[\-]\d$/; //标准编号

var genrePattern = /^[\u0000-\uffff]*$/;//流派

var languagePattern = /^[\u0000-\uffff]*$/;//流派

var placePattern = /^[\u0000-\uffff]*$/;//流派

var authorRolePattern = /^[\u0000-\uffff]*$/;//作者角色

var withdrawPricePattern = /^\d+(.\d{1,2})?$/;//提现价格

var totalPricePattern = /^[1-9]\d{0,7}([\.]\d{1,2})?$|^[0]([\.]\d{1,2})?$/;//全价

var oneDayPricePattern = /^[1-9]\d{0,2}([\.]\d{1,2})?$|^[0]([\.]\d{1,2})?$/;//按天价格

var optionCostPattern = /^[1-9]\d{0,7}([\.]\d{1,2})?$|^[0]([\.]\d{1,2})?$/;//附加价

//xy 增加打印价格正则表示
var printPattern = /^[1-9]\d{0,7}([\.]\d{1,2})?$|^[0]([\.]\d{1,2})?$/;

//qq号码的验证
var qqNumPattern=/^[1-9]\d{4,10}$/;

/*
 * 机构的正则
 * */
var organizationNamePattern = /^[a-zA-Z0-9\u4e00-\u9fa5]*$/;//机构名称

var abbreviationPattern = /^[a-zA-Z0-9\u4e00-\u9fa5]*$/;//机构简称

var telphonePattern = /^[1][358]\d([\-\.\s]?(\d{4})){2}$/;//联系人手机号

var organizationIdPattern = /^[\u0000-\uffff]*$/;//机构代码

//检测输入的字符中是否包含特殊字符及空格
var ilcode = /[\s\`\!\@\#\$\%\^\&\*\(\)\_\+\-\=\[\]\{\}\:\"\;\'\<\>\?\,\.\/\|\\\·\！\￥\…\（\）\—\【\】\、\；\‘\，\。\、\：\“\《\》\？]/g;