/**
	@author Nonlone
	@version 1.0
	@param {id,jQuery Obj} table
	@param {json} jsonData
	@param {json} table fields;
	@returns render colspan;
	table render;
*/
jQuery.fn.jtable = function(jsonData, fields) {

    var filterFields = ["theadtr", "theadth", "theadtd", "tbodytr", "tbodytd", "tfoottr", "tfoottd"];
    var totalColspan = 0;
    return this.each(function() {
        var $table = $(this);
        var $tablehead;
        var $tablebody;
        var $tablefoot;
        if (!$table instanceof jQuery) {
            return;
        }

        //init table
        $table.find("thead").remove();
        $table.find("tbody").remove();

        //init thead
        $tablehead = $("<thead></thead>");
        $table.append($tablehead);

        //init tbody		        
        $tablebody = $("<tbody></tbody>");
        $tablehead.after($tablebody);

        //render head;
        $tablehead.empty();
        var headhtml = "<tr>";
        //get headtrtemplate
        $.each(fields, function(i, field) {
            if (field.field == "theadtr") {
                headhtml = field.format;
                //replace mark
                $.each(fields, function(i, field) {
                    for (var key in field) {
                        headhtml = headhtml.replace(new RegExp("@" + key, "gm"), field[key]);
                    }
                });
                return;
            }
        });

        //render head
        var headthtemplate = "<th>";
        $.each(fields, function(i, field) {
            if (field.field == "theadth") {
                headthtemplate = field.format;
                return;
            }
        });
        $.each(fields, function(i, field) {
            var headth = headthtemplate;
            for (var i in filterFields) {
                if (field.field == filterFields[i]) {
                    return true;
                }
            }
            if (headth != "<th>") {
                for (var key in field) {
                    headth = headth.replace(new RegExp("@" + key, "gm"), field[key]);
                }
            }
            headth += "<span>" + field.name + "</span>";
            headth += "</th>";
            headhtml += headth;
        });
        headhtml += "</tr>";
        $tablehead.html(headhtml);

        $tablebody.empty();
        //render body
        var bodyhtml;
        var tbodytrtemplate = "<tr>";
        $.each(fields, function(i, field) {
            if (field.field == "tbodytr") {
                tbodytrtemplate = field.format;
                return;
            }
        });
        var tbodytdtemplate = "<td>";
        $.each(fields, function(i, field) {
            if (field.field == "tbodytd") {
                tbodytdtemplate = field.format;
                return;
            }
        });
        $.each(jsonData, function(i, data) {
            //render tr;
            var bodyhtml = tbodytrtemplate;
            for (var key in data) {
                bodyhtml = bodyhtml.replace(new RegExp("@" + key, "gm"), data[key]);
            }
            //render td;
            $.each(fields, function(k, field) {
                for (var fi in filterFields) {
                    if (field.field == filterFields[fi]) {
                        return true;
                    }
                }
                var tdhtml = tbodytdtemplate;
                for (var fkey in field) {
                    tdhtml = tdhtml.replace(new RegExp("@" + fkey, "gm"), field[fkey]);
                }
                if (field.format != null) {
                    tdhtml += field.format + "</td>";
                    for (var datakey in data) {
                        tdhtml = tdhtml.replace(new RegExp("@" + datakey, "gm"), data[datakey]);
                    }
                    tdhtml = tdhtml.replace(new RegExp("@index", "gm"), i);
                    tdhtml = tdhtml.replace(new RegExp("@datalength", "gm"), jsonData.length);
                } else {
                    //fill the field
                    tdhtml += "<span>" + data[field.field] + "</span></td>";
                }
                bodyhtml += tdhtml;
            });
            bodyhtml += "</tr>";
            $tablebody.append(bodyhtml);
        });
        //render foot
        var colspan = 0;
        $.each(fields, function(i, field) {
            for (var i in filterFields) {
                if (filterFields[i] == field.field)
                    return true;
            }
            colspan++;
        });
        totalColspan += colspan;
    });
    return totalColspan;
}

jQuery.fn.jexTable = function(option) {
    option = jQuery.extend({
        dynScrollStart: 8,
        dynCellWidth: 80,
        scrollBarHeight: 18,
        staWidth: 80,
        dynWidth: 680
    }, option || {});

    var staFields = option.staFields;
    var dynFields = option.dynFields;
    var data = option.data;
    var dynScrollStart = option.scrollStart;
    var scrollBarHeight = option.scrollBarHeight;
    var staWidth = option.staWidth;
    var dynWidth = option.dynWidth;
    var dynCellWidth = option.dynCellWidth;
    var staTable = option.staTable;
    var dynTable = dynTable;

    return this.each(function() {
        var $drawDiv = $(this);
        var $staTableContainer;
        var $staTable;
        var $dynContainer;
        var $dynTableScrollContainer;
        var $dynTableScrollBar;
        var $dynTableContainer;
        var $dynTable;


        if (staTable != null) {
            $staTbale = $drawDiv.find(staTable)[0].clone(true);
        }

        if (dynTable != null) {
            $dynTable = $drawDiv.find(dynTable)[0].clone(true);
        }

        $drawDiv.empty();

        //init sta Table Componment
        $drawDiv.find(".staTableContainer").remove();
        $drawDiv.find(".staTable").remove();
        //init staTableContainer        	
        $staTableContainer = $("<div></div>");
        $staTableContainer.addClass('staTableContainer');
        $staTableContainer.css({
            "float": "left",
            "margin-top": "1px",
            width: staWidth + "px"
        });

        //init staTable            
        if ($staTable == null) {
            $staTable = $("<table></table>");
        }
        $staTable.css("width", "100%");
        $staTable.addClass('staTable');
        //append to drawDiv
        $staTableContainer.append($staTable);
        $drawDiv.append($staTableContainer);

        //init dyn Table Componment
        $drawDiv.find(".dynTableContainer").remove();
        $drawDiv.find(".dynTableScrollContainer").remove();
        $drawDiv.find("dynTableScrollBar").remove();
        $drawDiv.find(".dynTable").remove();
        //init dyn Container
        $dynContainer = $("<div></div>");
        $dynContainer.addClass('dynContainer');
        $dynContainer.css({
            "float": "left",
            // "margin-left": "-1px",
            "width": dynWidth + "px"
        });
        //init dynTableScrollContainer
        $dynTableScrollContainer = $("<div></div>");
        $dynTableScrollContainer.addClass("dynTableScrollContainer");
        $dynTableScrollContainer.css({
            "width": dynWidth + "px",
            "overflow-x": "auto",
            "overflow-y": "auto"
        });
        $dynTableScrollBar = $("<div></div>");
        $dynTableScrollBar.addClass('dynTableScrollBar');
        $dynTableScrollBar.css({
            "width": dynWidth + "px",
            "height": "1px"
        });
        $dynTableScrollContainer.append($dynTableScrollBar);
        $dynTableContainer = $("<div></div>");
        $dynTableContainer.addClass('dynTableContainer');
        $dynTableContainer.css({
            "width": dynWidth + "px",
            "overflow-x": "auto",
            "overflow-y": "auto"
        });
        if ($dynTable == null) {
            $dynTable = $("<table></table>");
        }
        $dynTable.addClass('dynTable');
        $dynTable.css("width", "100%");
        //append to drawDiv
        $dynTableContainer.append($dynTable);
        $dynContainer.append($dynTableScrollContainer);
        $dynContainer.append($dynTableContainer);
        $drawDiv.append($dynContainer);

        var staColspan = $staTable.jtable(data, staFields).find('th').length;
        var dynColspan = $dynTable.jtable(data, dynFields).find('th').length;
        var ext = dynColspan - dynScrollStart;
        if (ext > 0) {
            //set scroll
            $dynTableScrollContainer.css("overflow-x", "scroll");
            $dynTableContainer.css("overflow-x", "scroll");
            //set margin-top
            $staTable.parent().css("margin-top", scrollBarHeight);
            //set width
            $dynTableScrollContainer.children("div").css("width", (dynWidth + ext * dynCellWidth) + "px");
            $dynTable.css("width", (dynWidth + ext * dynCellWidth) + "px");
        } else {
            //set scroll
            $dynTableScrollContainer.css("overflow-x", "auto");
            $dynTableContainer.css("overflow-x", "auto");
            //set margin-top
            $staTable.parent().css("margin-top", "1px");
            //set width
            $dynTableScrollContainer.children("div").css("width", dynWidth);
            $dynTable.css("width", dynWidth);
        }

        if (ext > 0) {
            //set event
            $drawDiv.find(".dynTableScrollContainer").scroll(function() {
                var left = $drawDiv.find(".dynTableScrollContainer").scrollLeft();
                $drawDiv.find(".dynTableContainer").scrollLeft(left);
            });
            $drawDiv.find(".dynTableContainer").scroll(function() {
                var left = $drawDiv.find(".dynTableContainer").scrollLeft();
                $drawDiv.find(".dynTableScrollContainer").scrollLeft(left);
            });
        }
    });
}