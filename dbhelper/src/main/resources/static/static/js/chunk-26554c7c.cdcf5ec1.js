(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-26554c7c"],{"192e":function(e,t,n){"use strict";var l=n("fe67"),a=n.n(l);a.a},d226:function(e,t,n){"use strict";n.r(t);var l=function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"content-wrapper"},[n("el-card",{attrs:{shadow:"always"}},[n("div",{attrs:{slot:"header"},slot:"header"},[e._v("\n      新建数据表\n    ")]),e._v(" "),n("el-form",{attrs:{"label-width":"90px"}},[n("el-form-item",{attrs:{label:"数据表名称"}},[n("el-input",{model:{value:e.tablename,callback:function(t){e.tablename=t},expression:"tablename"}})],1)],1),e._v(" "),n("el-table",{attrs:{border:"",data:e.tableData}},[n("el-table-column",{attrs:{prop:"column",label:"列名",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("el-input",{model:{value:t.row.column,callback:function(n){e.$set(t.row,"column",n)},expression:"scope.row.column"}})]}}])}),e._v(" "),n("el-table-column",{attrs:{prop:"type",label:"数据类型",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("el-select",{attrs:{placeholder:"数据类型"},model:{value:t.row.type,callback:function(n){e.$set(t.row,"type",n)},expression:"scope.row.type"}},e._l(e.typeList,function(e,t){return n("el-option",{key:t,attrs:{label:e,value:e}})}),1)]}}])}),e._v(" "),n("el-table-column",{attrs:{prop:"length",label:"长度",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("el-input",{model:{value:t.row.length,callback:function(n){e.$set(t.row,"length",e._n(n))},expression:"scope.row.length"}})]}}])}),e._v(" "),n("el-table-column",{attrs:{prop:"default",label:"默认值",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("el-input",{model:{value:t.row.default,callback:function(n){e.$set(t.row,"default",e._n(n))},expression:"scope.row.default"}})]}}])}),e._v(" "),n("el-table-column",{attrs:{prop:"isKey",label:"设为主键",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("el-checkbox",{model:{value:t.row.isKey,callback:function(n){e.$set(t.row,"isKey",n)},expression:"scope.row.isKey"}},[e._v("是")])]}}])}),e._v(" "),n("el-table-column",{attrs:{prop:"comment",label:"备注",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("el-input",{model:{value:t.row.comment,callback:function(n){e.$set(t.row,"comment",e._n(n))},expression:"scope.row.comment"}})]}}])}),e._v(" "),n("el-table-column",{attrs:{label:"操作",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("el-button",{attrs:{type:"danger",size:"small"},on:{click:function(n){return e.deleteColumn(t.row.index)}}},[e._v("删除")])]}}])})],1),e._v(" "),n("div",{staticStyle:{"margin-top":"20px"}},[n("i",{staticClass:"el-icon-circle-plus add-column",on:{click:e.addColumn}})]),e._v(" "),n("div",{staticStyle:{"text-align":"center"}},[n("el-button",{attrs:{type:"primary"},on:{click:e.createTable}},[e._v("\n        新建数据表\n      ")])],1)],1)],1)},a=[],o=(n("f763"),{data:function(){return{tablename:"",tableData:[{index:0,column:"",type:"",length:0,default:"",isKey:!1,comment:""}],currentIndex:0,typeList:["BIGINT","BINARY","BIT","BLOB","BLOB DATA TYPE","BOOLEAN","CHAR","CHAR BYTE","DATE","DATETIME","DEC","DECIMAL","DOUBLE","DOUBLE PRECISION","ENUM","FLOAT","INT","INTEGER","LONGBLOB","LONGTEXT","MEDIUMBLOB","MEDIUMINT","MEDIUMTEXT","SET DATA TYPE","SMALLINT","TEXT","TIME","TIMESTAMP","TINYBLOB","TINYINT","TINYTEXT","VARBINARY","VARCHAR","YEAR DATA TYPE"]}},methods:{addColumn:function(){this.currentIndex++,this.tableData.push({index:this.currentIndex,column:"",type:"",length:0,default:"",isKey:!1,comment:""})},deleteColumn:function(e){var t=this;this.tableData.forEach(function(n){if(n.index===e){var l=t.tableData.indexOf(n);t.tableData.splice(l,1)}})},createTable:function(){var e=this;this.$http.post("/new/table",{dbname:this.$route.query.dbname,tablename:this.tablename,tableData:this.tableData}).then(function(t){2e4===t.statusCode?e.$router.push({path:"/db/tables",query:{dbname:e.$route.query.dbname}}):e.$message({showClose:!0,message:t.statusMsg,type:"error"})}).catch(function(e){console.error(e)})}}}),r=o,c=(n("192e"),n("17cc")),s=Object(c["a"])(r,l,a,!1,null,"963935f0",null);t["default"]=s.exports},fe67:function(e,t,n){}}]);