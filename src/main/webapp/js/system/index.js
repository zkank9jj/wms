$(function() {
    // 加载日期
    loadDate();
    // ======================================
    // 显示隐藏侧边栏
    $("#show_hide_btn").click(function() {
        switchSysBar();
    });
    // ======================================
	//面板切换
    $("#TabPage2 li").click(function () {
    	var prefix = "/images/common/";
    	var suffix = ".jpg";
    	//重置所有图片和底色,再改动当前点击
		$.each($("#TabPage2 li"), function (index, ele) {
			$(ele).find("img").prop("src", prefix+(index + 1)+suffix);
            $(ele).removeClass("selected");
        });
		//切换当前的面板的图片
        $(this).find("img").prop("src", prefix+($(this).index()+1)+"_hover"+suffix);
        //切换底色
        $(this).addClass("selected");
        //修改模块名称的图片
        $("#nav_module img").prop("src", prefix+"module_"+($(this).index()+1)+".png");
        //修改显示的菜单
		var root = $(this).data("rootmenu");
        loadMenu(root);
    });

    //默认显示业务模块菜单
    loadMenu("business");
});

//zTree的系统配置
var setting = {
    //开启简单JSON格式
    data: {
        simpleData: {
            enable: true
        }
    },
	//给菜单节点绑定点击事件
    callback: {
        onClick: function (event, treeId, treeNode) {
            if (treeNode.action) { //非顶级菜单才执行访问界面
                var url= "/"+treeNode.action+".do";
                //重新显示当前的位置 父菜单 当前菜单
                $("#here_area").html("当前位置：" + treeNode.getParentNode().name + " > " + treeNode.name);
                //显示到网页的右边
                $("#rightMain").prop("src", url);
            }
        }
    },
	//异步加载菜单
    async: {
        enable: true,
        url: "/systemMenu/getMenusBySn.do",
        autoParam: ["sn=menuSn"]
    }
};

//菜单树的节点
var zNodes = {
    systemManage: [
        {id:1, pId:0, name: "系统模块", sn: "system", isParent: true}
    ],
    business: [
        {id:2, pId:0, name: "业务模块", sn: "business", isParent: true}
    ],
    charts: [
        {id:3, pId:0, name: "报表模块", sn: "chart", isParent: true}
    ]
};

//加载菜单,显示菜单树
function loadMenu(root) {
    $.fn.zTree.init($("#dleft_tab1"), setting, zNodes[root]);
}

//加载当前日期
function loadDate() {
	var time = new Date();
	var myYear = time.getFullYear();
	var myMonth = time.getMonth() + 1;
	var myDay = time.getDate();
	if (myMonth < 10) {
		myMonth = "0" + myMonth;
	}
	document.getElementById("day_day").innerHTML = myYear + "." + myMonth + "."
			+ myDay;
}

/**
 * 隐藏或者显示侧边栏
 * 
 */
function switchSysBar(flag) {
	var side = $('#side');
	var left_menu_cnt = $('#left_menu_cnt');
	if (flag == true) { // flag==true
		left_menu_cnt.show(500, 'linear');
		side.css({
			width : '280px'
		});
		$('#top_nav').css({
			width : '77%',
			left : '304px'
		});
		$('#main').css({
			left : '280px'
		});
	} else {
		if (left_menu_cnt.is(":visible")) {
			left_menu_cnt.hide(10, 'linear');
			side.css({
				width : '60px'
			});
			$('#top_nav').css({
				width : '100%',
				left : '60px',
				'padding-left' : '28px'
			});
			$('#main').css({
				left : '60px'
			});
			$("#show_hide_btn").find('img').attr('src',
					'/images/common/nav_show.png');
		} else {
			left_menu_cnt.show(500, 'linear');
			side.css({
				width : '280px'
			});
			$('#top_nav').css({
				width : '77%',
				left : '304px',
				'padding-left' : '0px'
			});
			$('#main').css({
				left : '280px'
			});
			$("#show_hide_btn").find('img').attr('src',
					'/images/common/nav_hide.png');
		}
	}
}
