var table;


function storey_stat(data) {
  var chartDom = document.getElementById('storey-stat');
  var myChart = echarts.init(chartDom);
  var option;

  var nameData = [];
  var valueDate = [];

  data.forEach(function (item, index) {
    nameData.push("第 " + item.name + " 层");
    valueDate.push(item.value);
  })


  option = {
    xAxis: {
      type: 'category',
      data: nameData
      // data: [1, 2, 3, 4, 5, 6, 7, 8]
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        data: valueDate,
        type: 'bar'
      }
    ]
  };

  option && myChart.setOption(option);

}


$(function () {

  $('.select2').select2();

  //Date range picker
  var dater = $('#analyse-reservation').daterangepicker({
    locale: {
      format: 'YYYY/MM/DD',
      separator: '-'
    }
  });

  dater.on('apply.daterangepicker', function (ev, picker) {
    // 获取用户选择的日期范围的值
    console.log('开始日期: ' + picker.startDate.format('YYYY/MM/DD'));
    console.log('结束日期: ' + picker.endDate.format('YYYY/MM/DD'));

    console.log(dater.val());
  });


  $("#analyse-search").click(function () {
    var token = localStorage.getItem("token");
    if (token == undefined || token == null || token.length == 0) {
      window.parent.window.location.href = "/login.html";
    }

    var categorize = $("#analyse-categorize").val();
    var interval = $("#analyse-interval").val();
    var reservation = $("#analyse-reservation").val();


    if (categorize == undefined || categorize == null || categorize.length == 0) {
      alert("必须选择类型维度");
      return;
    }

    if (interval == undefined || interval == null || interval.length == 0) {
      alert("必须选择时间维度");
      return;
    }

    if (reservation == undefined || reservation == null || reservation.length == 0) {
      alert("必须选择时间范围");
      return;
    }


    var obj = {
      categorize: categorize,
      interval: interval,
      reservation: reservation,

    }

    $.ajax({
      method: 'POST',
      url: "/zmy/airs-analyse",
      contentType: 'application/json',
      headers: {
        token: token
      },
      processData: false,
      data: JSON.stringify(obj),
      success: function (resp) {
        if (resp.code == 200) {
          // window.location.href = "/login.html"
          alert("add device : " + number + " success");
          location.reload();
        } else if (resp.code == 302) {
          window.parent.window.location.href = "/login.html";
        } else {
          alert("add device : " + number + " failed , desc: " + resp.desc);
        }
      }
    });


  });


  table = $("#analyse-table").DataTable({
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
  // table.buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');
});
