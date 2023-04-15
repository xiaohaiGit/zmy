var table;

function deleteAir(number) {
  var yes = confirm("是否要删除空调: " + number);
  if (!yes) {
    return;
  }

  var token = localStorage.getItem("token");
  if (token == undefined || token == null || token.length == 0){
    window.parent.window.location.href = "/login.html";
  }
  $.ajax({
    method: 'DELETE',
    url: "/zmy/air",
    // contentType: 'application/json',
    headers: {
      token: token
    },
    data: {
      number: number
    },
    success: function (resp) {
      if (resp.code == 200) {
        // window.location.href = "/login.html"

        alert("delete air : " + number + " success , 请刷新表格");
      } else if (resp.code == 302) {
        window.parent.window.location.href = "/login.html";
      } else {
        alert("delete air : " + number + " failed , desc: " + resp.desc);
      }
    }
  });

}


$(function () {
  table = $("#example1").DataTable({
    "ajax": {
      "url": "/zmy/airs",
      "method": "GET",
      "headers": {
        token: localStorage.getItem("token")
      },
      dataSrc: "data",
    },
    "responsive": true,
    "lengthChange": false,
    "autoWidth": false,
    "buttons": ["copy", "csv", "excel", "pdf", "print", "colvis"],
    "order": [[0, "asc"]],
    "paging": true,
    "columns": [
      {
        data: "id",
        visible: false
      },
      {
        data: "region",
        render: function (data, type, row) {
          if (data == 1) {
            return "A区"
          } else if (data == 2) {
            return "B区";
          } else if (data == 3) {
            return "C区";
          }
          return "other";
        }
      },
      {
        data: "type",
        render: function (data, type, row) {
          if (data == 1) {
            return "柜式"
          } else if (data == 2) {
            return "挂式";
          }
          return "other";
        }
      },
      {
        "data": "number",
        render: function (data, type, row) {
          return "<b>" + data + "</b>";
        }
      },
      {
        "data": "building",
        render: function (data, type, row) {
          if (data == 1) {
            return "1号楼"
          } else if (data == 2) {
            return "2号楼";
          }
          return "other";
        }
      },
      {
        "data": "storey",
        render: function (data, type, row) {
          return "第" + data + "层";
        }
      },
      {
        "data": "desc"
      },
      {
        "data": "createTime",
      },
      {
        "data": "updateTime",
      },
      {
        "data": null,
        render: function (data, type, row) {
          console.log(row);
          return "<button type=\"button\" class=\"btn btn-block btn-danger\" onclick='deleteAir(\"" + row.number + "\")'>删除</button>";
        }
      },
    ]
  });
  table.buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');
});
