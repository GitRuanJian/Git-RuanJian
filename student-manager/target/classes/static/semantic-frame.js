/*!
 * # Semantic Dynamic Data Frame v1.0.0
 * Based on Semantic UI 2.4.1
 * Created by Chenxiao Lin on 11th May, 2021
 */

;(function ($, document, undefined) {
    'use strict';

    let host;
    
    $.isType = function (obj, t) {
        return obj && toString.call(obj) === `[object ${t}]`;
    }

    $.setHost = function (h) {
        if ($.isType(h, 'String')) {
            if (!h.toLowerCase().startsWith('http')) h = 'http://' + h;
            if (h.endsWith('/')) h = h.substring(0, h.length - 1);
            host = h;
        }
    }

    $.clone = function (value) {
        if ($.isType(value, 'Object')) {
            let obj = {};
            $.each(value, function(k, v) {
                obj[k] = v;
            });
            return obj;
        }
        else if ($.isType(value, 'Array')) {
            let arr = [];
            for (let v of value) arr.push($.clone(v));
            return arr;
        }
        else return value;
    }

    $.toast = function(parameters) {
        if (!parameters) return;
        let html = `<div class="ui${$.isType(parameters, 'Object') && parameters.type ? ' ' + parameters.type : ''} message transition hidden"><i class="close icon"></i><div class="header">${$.isType(parameters, 'Object') ? parameters.text : parameters}</div></div>`;
        let $module = $('#ui-messages');
        if ($module.length === 0) {
            $('body').append('<div id="ui-messages" style="position: absolute;width: 20%;top: 24px;left: 12px;z-index: 100"></div>');
            $module = $('#ui-messages');
        }
        $module.append(html);
        let $msg = $module.children('div:last');
        $msg.find('.close')
            .on('click', function() {
                $(this).closest('.message').transition('fade');
            })
        ;
        $msg.transition('fade left');
        setTimeout(function () {
            if ($msg.hasClass('visible')) $msg.transition({animation: 'fade down', duration: 600});
        }, 8000);
    };

    $.error = function(text) {
        if (text) $.toast({type: 'error', text});
    }

    $.success = function(text) {
        if (text) $.toast({type: 'success', text});
    }

    $.fn.parseForm = function(value) {
        let obj = value && $.isType(value, 'Object') ? value : {},
            array = this.serializeArray();
        $(array).each(function() {
            obj[this.name] = this.value;
        });
        $(this).find('.dropdown').each(function () {
            let $module = $(this);
            if ($module.hasClass('multiple')) {
                let key;
                if (!(key = $module.attr('name'))) {
                    key = $module.find('select').attr('name');
                }
                if (key) obj[key] = $module.dropdown('get value');
            }
        });
        return obj;
    };

    $.fn.setForm = function(value) {
        let $form = $(this);
        if ($.isType(value, 'Object')) $.each(value, function(name, v) {
            let $item = $form.find(`input[name="${name}"]`);
            if ($item.length > 0) $item.val(v);
            else if (($item = $form.find(`select[name="${name}"]`)).length > 0) {
                if ($.isType(v, 'Array')) {
                    let a = [];
                    for (let i of v) if (i || i === 0) a.push(i.toString());
                    $item.dropdown('set selected', a);
                }
                else $item.dropdown('set selected', v.toString());
            }
        });
    };
    
    $.fn.buildForm = function (fields) {
        if (!$.isType(fields, 'Array')) return;
        let $module = $(this);
        let form = '';
        for (let field of fields) {
            if (field.key) {
                let t = field.label ? field.label : field.key;
                if ($.isType(field.options, 'Array')) {
                    let options = '';
                    for (let v of field.options) options += `<option value="${v.value}">${v.text}</option>`;
                    form += `<div class="field"><label>${t}</label><select class="ui search dropdown"${field.multiple ? ' multiple=""' : ''} name="${field.key}">${options}</select></div>`;
                }
                else if (field.number) {
                    form += `<div class="field"><label>${t}</label><input type="number" name="${field.key}" placeholder="${t}"></div>`;
                }
                else {
                    form += `<div class="field"><label>${t}</label><input type="text" name="${field.key}" placeholder="${t}"></div>`;
                }
            }
        }
        $module.html(form);
        $module.find('.dropdown').dropdown();
    }

    $.dialog = function(parameters) {
        let header = '提示', content = '请确认', $module, $form,
            part1 = '<div class="ui mini modal"><div class="header">',
            part2 = '</div><div class="content">',
            part3 = '</div><div class="actions"><div class="ui primary approve button">确认</div><div class="ui primary deny button">取消</div></div></div>';
        if ($.isType(parameters, 'Object')) {
            if ($.isType(parameters.title, 'String')) header = parameters.title;
            if ($.isType(parameters.fields, 'Array')) {
                content = '<form class="ui form"></form>';
                $('body').append(part1 + header + part2 + content + part3);
                $module = $('.ui.mini.modal:last');
                $form = $module.find('.form');
                $form.buildForm(parameters.fields);
                if (parameters.data) $form.setForm(parameters.data);
            }
            else if ($.isType(parameters.message, 'String')) content = parameters.message;
        }
        if (!$module) {
            $('body').append(part1 + header + part2 + content + part3);
            $module = $('.ui.mini.modal:last');
        }
        $module.modal({
            closable: false,
            onApprove: function ($ele) {
                if ($.isType(parameters.callback, 'Function')) {
                    if ($form) {
                        let formVal;
                        if (parameters.data) formVal = $form.parseForm($.clone(parameters.data));
                        else formVal = $form.parseForm();
                        parameters.callback(formVal);
                    }
                    else parameters.callback($ele);
                }
            },
            onHidden: function () {
                $module.remove();
            }
        }).modal('show');
    };

    $.request = function (parameters) {
        if ($.isType(parameters, 'Object')) {
            let params = {
                on: 'now',
                method: parameters.method ? parameters.method : 'post',
                beforeSend: function (settings) {
                    if (parameters.data) settings.data = parameters.data;
                    return settings
                },
                onResponse: function (response) {
                    if (!response) $.error('后台未启动');
                    else if (!response.code) {
                        if ($.isType(parameters.callback, 'Function'))
                            parameters.callback(response);
                    }
                    else if (response.message) $.error(response.message);
                    else $.error('未知错误');
                    return response;
                },
                onError: function (errorMessage) {
                    $.error(errorMessage);
                }
            }
            if ($.isType(parameters.url, 'String')) params.url = (host ? host : '') + parameters.url;
            else if ($.isType(parameters.action, 'String')) params.action = parameters.action;
            else return;
            $(document).api(params);
        }
    }

    $.fn.feed = function (parameters) {
        let $modules = $(this);
        if ($modules.length === 0) return;
        else if ($modules.length > 1) $modules = $modules.eq(0);
        if (!$modules.hasClass('table')) return;
        let data = [], table, utils, apis, proxy,
            currentPage = parameters.currentPage,
            totalPage = parameters.totalPage,
            numPerPage = parameters.numPerPage;

        utils = {
            isPositiveNumber: function(value) {
                return $.isType(value, 'Number') && !isNaN(value) && isFinite(value) && value > 0;
            },
            request: function (action, callback, data) {
                $.request({
                    action,
                    data,
                    callback
                });
            }
        };

        apis = {
            requestData: function () {
                let condition = table.condition;
                if (condition.column === '0') condition.column = '';
                condition.offset = numPerPage * (currentPage - 1);
                condition.limit = numPerPage;
                utils.request('list', function (resp) {
                    totalPage = Math.ceil(resp.total / numPerPage);
                    data.splice(0, data.length);
                    for (let v of resp.data) data.push(v);
                    $modules.find('tfoot .menu').html(table.buildPager());
                    table.pagerClick();
                }, condition);
            },
            addData: function (value, callback) {
                utils.request('add', function (resp) {
                    value.id = resp.data;
                    callback();
                }, value);
            },
            editData: function (value, callback) {
                utils.request('edit', function () {
                    callback();
                }, value);
            },
            deleteData: function (values, callback) {
                let ids = [];
                if ($.isType(values, 'Array')) {
                    for (let v of values) if (v && v.id) ids.push(v.id);
                }
                else ids.push(values.id);
                utils.request('delete', function () {
                    callback();
                }, {ids});
            }
        };

        proxy = {
            set: function (target, key, value, receiver) {
                if (!(Array.isArray(target) && key === 'length')) {
                    if (target[key] === undefined) {
                        let $tbody = $modules.find('tbody');
                        $tbody.append(table.buildTr(value));
                        let $tr = $tbody.find('tr:last');
                        $tr.find('a').bind('click', table.rowClick);
                        if (table.tableButtons.length > 0) {
                            $tr.find('.checkbox:first').checkbox({
                                onChange: table.checkboxChanged
                            });
                        }
                    }
                    else {
                        let $tr = $modules.find('tbody tr').eq(key);
                        let $tds = $tr.find('td');
                        let i = 0;
                        if (table.tableButtons.length > 0) {
                            i++;
                            $tds.eq(0).find('.checkbox:first').checkbox(`set unchecked`);
                            table.$checkBox.checkbox(`set unchecked`);
                            $modules.find('tfoot .button').addClass('disabled');
                        }
                        for (let col of table.columns) {
                            let cell = '';
                            if ($.isType(col.handler, 'Function')) cell = col.handler(value[col.key]);
                            else if (value[col.key] !== undefined && value[col.key] !== null) cell = value[col.key];
                            $tds.eq(i++).html(cell);
                        }
                        if ($.isType(parameters.rowClass, 'Function')) {
                            $tr.removeClass();
                            let classes = parameters.rowClass(value, key);
                            if ($.isType(classes, 'String')) $tr.addClass(classes);
                        }
                    }
                }
                return Reflect.set(target, key, value, receiver);
            },
            deleteProperty(target, key) {
                $modules.find('tbody tr').eq(key).remove();
                return Reflect.deleteProperty(target, key);
            }
        };

        table = {
            $checkBox: null,
            condition: {},
            columns: [],
            tableButtons: [],
            rowButtons: [],
            rowTd: '',
            init: function () {
                let thead = '', tbody = '', tfoot = '', queryForm = '';
                queryForm = '<div class="ui small form"><div class="fields">';
                if ($.isType(parameters.query, 'Object') && $.isType(parameters.dataApi, 'String')) {
                    let options = '<option value="0">全部列表</option>';
                    $.each(parameters.query, function(k, v) {
                        options += `<option value="${k}">${v}</option>`;
                    });
                    queryForm += `<div class="inline field"><label>搜索: </label><select class="ui dropdown" name="column">${options}</select></div><div class="field disabled"><input type="text" name="value" placeholder="搜索内容"></div><div class="field"><button class="ui primary button">查询</button></div>`;
                }
                if ($.isType(parameters.addApi, 'String') && $.isType(parameters.formFields, 'Array')) {
                    queryForm += '<div class="field"><button class="ui teal button">添加</button></div>';
                }
                if (queryForm.endsWith('</div>')) {
                    queryForm += '</div></div>';
                    $modules.before(queryForm);
                }
                if (!utils.isPositiveNumber(currentPage)) currentPage = 1;
                if (!utils.isPositiveNumber(numPerPage)) numPerPage = 20;
                if ($.isType(parameters.columns, 'Array')) {
                    for (let v of parameters.columns) {
                        if ($.isType(v, 'Object') && $.isType(v.text, 'String') && $.isType(v.key, 'String')) {
                            table.columns.push(v);
                            thead += `<th>${v.text}</th>`;
                        }
                    }
                }
                else if ($.isType(parameters.columns, 'Object')) {
                    for (let key in parameters.columns) {
                        let text = parameters.columns[key];
                        table.columns.push({key, text});
                        thead += `<th>${text}</th>`;
                    }
                }
                if (parameters.tableButtons !== false) {
                    if ($.isType(parameters.deleteApi, 'String')) {
                        table.tableButtons.push({text: '删除', classes: 'disabled',
                            handler: function (values, indies) {
                                $.dialog({
                                    title: '提示',
                                    message: '确认删除选中数据',
                                    callback: function () {
                                        apis.deleteData(values, function () {
                                            indies.sort((a, b) => b - a);
                                            for (let i of indies) data.splice(i, 1);
                                            $.success('删除成功');
                                        });
                                    }
                                });
                            }});
                        tfoot += `<div class="ui button disabled">删除</div>`;
                    }
                    if ($.isType(parameters.tableButtons, 'Array')) {
                        for (let v of parameters.tableButtons) {
                            if ($.isType(v, 'Object') && $.isType(v.text, 'String') &&
                                $.isType(v.handler, 'Function')) {
                                table.tableButtons.push(v);
                                tfoot += `<div class="ui button disabled${$.isType(v.classes, 'String') ? ' ' + v.classes : ''}">${v.text}</div>`;
                            }
                        }
                    }
                }
                if (parameters.rowButtons !== false) {
                    if ($.isType(parameters.editApi, 'String') && $.isType(parameters.formFields, 'Array')) {
                        table.rowButtons.push({text: '编辑',
                            handler: function (value, index) {
                                $.dialog({title: '编辑', fields: parameters.formFields, data: value,
                                    callback: function (formVal) {
                                        apis.editData(formVal, function () {
                                            data[index] = formVal;
                                            $.success('修改成功');
                                        });
                                    }});
                            }});
                        table.rowTd += `<a href="#"${table.rowTd ? ` style="margin-left: 8px"` : ''}>编辑</a>`
                    }
                    if ($.isType(parameters.deleteApi, 'String')) {
                        table.rowButtons.push({text: '删除', color: 'red',
                            handler: function (value, index) {
                                $.dialog({
                                    title: '提示',
                                    message: '确认删除该数据',
                                    callback: function () {
                                        apis.deleteData(value, function () {
                                            data.splice(index, 1);
                                            $.success('删除成功');
                                        });
                                    }
                                });
                            }});
                        table.rowTd += `<a href="#"${table.rowTd ? ` style="margin-left: 8px;color: red"` : ' style="color: red"'}>删除</a>`
                    }
                    if ($.isType(parameters.rowButtons, 'Array')) {
                        for (let v of parameters.rowButtons) {
                            if ($.isType(v, 'Object') && $.isType(v.text, 'String') &&
                                $.isType(v.handler, 'Function')) {
                                table.rowButtons.push(v);
                                table.rowTd += `<a href="#"${table.rowTd ? ` style="margin-left: 8px${$.isType(v.color, "String") ? ';color: ' + v.color : ''}"` : $.isType(v.color, "String") ? ` style="color: ${v.color}"` : ''}>${v.text}</a>`
                            }
                        }
                    }
                }
                if (table.tableButtons.length > 0) thead = '<th></th>' + thead;
                if (table.rowButtons.length > 0) {
                    thead += '<th></th>';
                    table.rowTd = '<td>' + table.rowTd + '</td>';
                }
                thead = '<thead><tr>' + thead + '</tr></thead>';
                tbody = '<tbody>' + tbody + '</tbody>';
                tfoot = '<div class="ui right floated pagination menu">' + table.buildPager() + '</div>' + tfoot;
                let colNum = table.columns.length;
                if (table.rowButtons.length > 0) colNum++;
                tfoot = `<tfoot class="full-width"><tr>${table.tableButtons.length > 0 ? '<th><div class="ui fitted checkbox"><input type="checkbox"> <label></label></div></th>' : ''}${colNum > 0 ? `<th${colNum > 1 ? ` colspan="${colNum}"` : ''}>${tfoot}</th>` : ''}</tr></tfoot>`;
                if (table.tableButtons.length > 0) {
                    if (!$modules.hasClass('definition')) $modules.addClass('definition');
                }
                else if ($modules.hasClass('definition')) $modules.removeClass('definition');
                $modules.html(thead + tbody + tfoot);
            },
            bind: function () {
                let $queryForm = $modules.prev();
                if ($.isType(parameters.query, 'Object')) {
                    let $dropdown = $queryForm.find('.dropdown');
                    let $input = $queryForm.find('input');
                    $dropdown.dropdown({
                        onChange: function (value) {
                            if (value !== '0') $queryForm.find('.field').eq(1).removeClass('disabled');
                            else $queryForm.find('.field').eq(1).addClass('disabled');
                        }
                    });
                    $queryForm.find('.primary.button').bind('click', function () {
                        currentPage = 1;
                        table.condition.column = $dropdown.dropdown('get value');
                        table.condition.value = $input.val();
                        if (table.condition.column === '0') {
                            table.condition.column = '';
                        }
                        apis.requestData();
                    });
                }
                $queryForm.find('.teal.button').bind('click', function () {
                    $.dialog({title: '新建', fields: parameters.formFields,
                        callback: function (formVal) {
                            apis.addData(formVal, function () {
                                $.success("添加成功");
                            });
                        }});
                });
                if (table.tableButtons.length > 0) {
                    $modules.find('tbody .checkbox').checkbox({onChange: table.checkboxChanged});
                    table.$checkBox = $modules.find('tfoot .checkbox');
                    table.$checkBox.checkbox('set unchecked');
                    table.$checkBox.bind('click', function () {
                        let $module = $(this);
                        if ($module.checkbox('is checked')) {
                            $modules.find('tfoot .button').removeClass('disabled');
                            $('.table').find('tbody .checkbox').checkbox('set checked');
                        }
                        else {
                            $modules.find('tfoot .button').addClass('disabled');
                            $('.table').find('tbody .checkbox').checkbox('set unchecked');
                        }
                    });
                    $modules.find('tfoot .button').bind('click', function () {
                        let $module = $(this);
                        let i = $module.index() - 1;
                        if (i < 0 || i >= table.tableButtons.length) return;
                        let selectedData = [];
                        let selectedIndex = [];
                        $module.parents('tfoot').prev().find('.ui.checkbox').each(function () {
                            let $checkbox = $(this);
                            if ($checkbox.parents('td').index() === 0 && $checkbox.checkbox('is checked')) {
                                let j = $checkbox.parents('tr').index();
                                if (j >= 0 && j < data.length) selectedIndex.push(j);
                            }
                        });
                        selectedIndex.sort((a, b) => b - a);
                        for (let idx of selectedIndex) selectedData.push(data[idx]);
                        if (selectedData.length > 0) table.tableButtons[i].handler(selectedData, selectedIndex);
                    });
                }
                $modules.find('tbody a').bind('click', table.rowClick);
            },
            setup: function () {
                $.fn.api.settings.api = {
                    'add': (host ? host : '') + parameters.addApi,
                    'edit': (host ? host : '') + parameters.editApi,
                    'delete': (host ? host : '') + parameters.deleteApi,
                    'list': (host ? host : '') + parameters.dataApi
                };
            },
            acquire: function () {
                data = new Proxy(data, proxy);
                if ($.isType(parameters.data, 'Array')) {
                    for (let v of parameters.data) {
                        if (!$.isType(v, 'Object')) continue;
                        data.push(v);
                    }
                    $modules.find('tfoot .menu').html(table.buildPager());
                    table.pagerClick();
                }
                else if ($.isType(parameters.dataApi, 'String')) {
                    apis.requestData();
                }
            },
            buildTr: function(v) {
                let tr = '<tr>';
                if ($.isType(parameters.rowClass, 'Function')) {
                    let classes = parameters.rowClass(v);
                    if ($.isType(classes, 'String')) tr = `<tr class="${classes}">`;
                }
                if (table.tableButtons.length > 0) tr += '<td class="collapsing"><div class="ui fitted checkbox"><input type="checkbox"> <label></label></div></td>';
                for (let col of table.columns) {
                    let cell = '';
                    if ($.isType(col.handler, 'Function')) cell = col.handler(v[col.key]);
                    else if (v[col.key] !== undefined && v[col.key] !== null) cell = v[col.key];
                    if (col.align === 'center' || col.align === 'right') tr += `<td class="${col.align} aligned">`;
                    else tr += '<td>';
                    tr += cell + '</td>';
                }
                if (table.rowButtons.length > 0) tr += table.rowTd;
                return tr + '</tr>';
            },
            buildPager: function () {
                if (!utils.isPositiveNumber(totalPage)) totalPage = 1;
                if (!utils.isPositiveNumber(currentPage)) currentPage = 1;
                else if (currentPage > totalPage) currentPage = totalPage;
                let pager = '';
                if (totalPage <= 7) {
                    for (let i = 1; i <= totalPage; i++) pager += `<a class="${i === currentPage ? 'active ' : ''}item">${i}</a>`;
                }
                else if (currentPage <= 4) {
                    for (let i = 1; i <= 5; i++) pager += `<a class="${i === currentPage ? 'active ' : ''}item">${i}</a>`;
                    pager += `<a class="icon disabled item"><i class="ellipsis horizontal icon"></i></a><a class="item">${totalPage}</a>`;
                }
                else if (currentPage >= totalPage - 3) {
                    pager += '<a class="item">1</a><a class="icon disabled item"><i class="ellipsis horizontal icon"></i></a>';
                    for (let i = totalPage - 4; i <= totalPage; i++) pager += `<a class="${i === currentPage ? 'active ' : ''}item">${i}</a>`;
                }
                else {
                    pager += '<a class="item">1</a><a class="icon item"><i class="ellipsis horizontal icon"></i></a>';
                    for (let i = currentPage - 1; i <= currentPage + 1; i++) pager += `<a class="${i === currentPage ? 'active ' : ''}item">${i}</a>`;
                    pager += `<a class="icon disabled item"><i class="ellipsis horizontal icon"></i></a><a class="item">${totalPage}</a>`;
                }
                return pager;
            },
            checkboxChanged: function () {
                let num = 0;
                $modules.find('tbody .checkbox').each(function (i) {
                    let $module = $(this);
                    if ($module.checkbox('is checked')) num++;
                });
                let $buttons = $modules.find('tfoot .button');
                $buttons.removeClass('disabled');
                if (num < data.length) {
                    table.$checkBox.checkbox('set unchecked');
                    if (num === 0) $buttons.addClass('disabled');
                }
                else table.$checkBox.checkbox('set checked');
            },
            pagerClick: function () {
                $modules.find('tfoot .item').bind('click', function () {
                    let $module = $(this);
                    let page = parseInt($module.html());
                    if (isNaN(page) || page < 1 || page > totalPage || page === currentPage) return;
                    currentPage = page;
                    if ($.isType(parameters.pageHandler, 'Function')) {
                        $modules.find('tfoot .item').removeClass('active');
                        $module.addClass('active');
                        parameters.pageHandler(page);
                    }
                    else if ($.isType(parameters.dataApi, 'String')) {
                        apis.requestData();
                    }
                });
            },
            rowClick: function () {
                let $module = $(this);
                let i = $module.index();
                let j = $module.parents('tr').index();
                if (i >= 0 && i < table.rowButtons.length && j >= 0 && j < data.length) table.rowButtons[i].handler(data[j], j);
            }
        };

        if (!$.isType(parameters, 'Object')) {
            console.error('参数不是合法对象');
            return;
        }

        table.setup();
        table.init();
        table.bind();
        table.acquire();

        return data;
    };
})( jQuery, document );
