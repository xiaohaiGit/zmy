/*
 * Author: Abdullah A Almsaeed
 * Date: 4 Jan 2014
 * Description:
 *      This is a demo file used only for the main dashboard (index.html)
 **/

/* global moment:false, Chart:false, Sparkline:false */

function region_stat(data) {

  var chartDom = document.getElementById('region-stat');
  var myChart = echarts.init(chartDom);
  var option;

  for (let i = 0; i < data.length; i++) {
    if (data[i].name === 1) {
      data[i].name = "A 区";
    } else if (data[i].name === 2) {
      data[i].name = "B 区";
    } else if (data[i].name === 3) {
      data[i].name = "C 区";
    }
  }

  option = {
    title: {
      text: '空调区域分布数量一览',
      subtext: '实时数据',
      left: 'center'
    },
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: 'Access From',
        type: 'pie',
        radius: '50%',
        data: data,
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  };

  option && myChart.setOption(option);
}

function building_stat(data) {

  var chartDom = document.getElementById('building-stat');
  var myChart = echarts.init(chartDom);
  var option;


  for (let i = 0; i < data.length; i++) {
    if (data[i].name === 1) {
      data[i].name = "1 号楼";
    } else if (data[i].name === 2) {
      data[i].name = "2 号楼";
    }
  }


  option = {
    tooltip: {
      trigger: 'item'
    },
    legend: {
      top: '5%',
      left: 'center'
    },
    series: [
      {
        name: 'Access From',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: true,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 40,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: data
      }
    ]
  };

  option && myChart.setOption(option);
}

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

function type_stat(data) {
  var chartDom = document.getElementById('type-stat');
  var myChart = echarts.init(chartDom);
  var option;

  var newData = [];
  var sum = 0;
  data.forEach(function (item, index) {
    if (item.name === 1) {
      item.name = "柜式";
    } else if (item.name === 2) {
      item.name = "挂式";
    }
    newData.push(item);
    sum += item.value;
  });

  var last = {
    // make an record to fill the bottom 50%
    value: sum,
    itemStyle: {
      // stop the chart from rendering this piece
      color: 'none',
      decal: {
        symbol: 'none'
      }
    },
    label: {
      show: false
    }
  };

  newData.push(last);

  option = {
    tooltip: {
      trigger: 'item'
    },
    legend: {
      top: '5%',
      left: 'center',
      // doesn't perfectly work with our tricks, disable it
      selectedMode: false
    },
    series: [
      {
        name: 'Access From',
        type: 'pie',
        radius: ['40%', '70%'],
        center: ['50%', '70%'],
        // adjust the start angle
        startAngle: 180,
        label: {
          show: true,
          formatter(param) {
            // correct the percentage
            return param.name + ' (' + param.percent * 2 + '%)';
          }
        },
        data: newData
      }
    ]
  };

  option && myChart.setOption(option);
}

var token = localStorage.getItem("token");
if (token == undefined || token == null || token.length === 0) {
  window.parent.window.location.href = "/login.html";
}

$(function () {


  $.ajax({
    method: 'GET',
    url: "/zmy/airs-count",
    // contentType: 'application/json',
    headers: {
      token: token
    },
    success: function (resp) {
      if (resp.code == 200) {
        // window.location.href = "/login.html"
        $("#air-count").text(resp.data);

      } else if (resp.code == 302) {
        window.parent.window.location.href = "/login.html";
      }
    }
  });

  $.ajax({
    method: 'GET',
    url: "/zmy/airs-usage-rate",
    // contentType: 'application/json',
    headers: {
      token: token
    },
    success: function (resp) {
      if (resp.code == 200) {
        // window.location.href = "/login.html"
        $("#air-usage-rate").html(resp.data + "<sup style=\"font-size: 20px\">%</sup>");

      } else if (resp.code == 302) {
        window.parent.window.location.href = "/login.html";
      }
    }
  });

  $.ajax({
    method: 'GET',
    url: "/zmy/airs-ele-usage",
    // contentType: 'application/json',
    headers: {
      token: token
    },
    success: function (resp) {
      if (resp.code == 200) {
        // window.location.href = "/login.html"
        $("#air-ele-total").text(resp.data.total);
        $("#air-ele-avg").text(resp.data.avg);


      } else if (resp.code == 302) {
        window.parent.window.location.href = "/login.html";
      }
    }
  });


  $.ajax({
    method: 'GET',
    url: "/zmy/air-region-stat",
    // contentType: 'application/json',
    headers: {
      token: token
    },
    success: function (resp) {
      if (resp.code == 200) {
        // window.location.href = "/login.html"
        region_stat(resp.data);
      } else if (resp.code == 302) {
        window.parent.window.location.href = "/login.html";
      }
    }
  });


  $.ajax({
    method: 'GET',
    url: "/zmy/air-building-stat",
    // contentType: 'application/json',
    headers: {
      token: token
    },
    success: function (resp) {
      if (resp.code === 200) {
        // window.location.href = "/login.html"
        building_stat(resp.data);
      } else if (resp.code === 302) {
        window.parent.window.location.href = "/login.html";
      }
    }
  });


  $.ajax({
    method: 'GET',
    url: "/zmy/air-storey-stat",
    // contentType: 'application/json',
    headers: {
      token: token
    },
    success: function (resp) {
      if (resp.code === 200) {
        // window.location.href = "/login.html"
        storey_stat(resp.data);
      } else if (resp.code === 302) {
        window.parent.window.location.href = "/login.html";
      }
    }
  });


  $.ajax({
    method: 'GET',
    url: "/zmy/air-type-stat",
    // contentType: 'application/json',
    headers: {
      token: token
    },
    success: function (resp) {
      if (resp.code === 200) {
        // window.location.href = "/login.html"
        type_stat(resp.data);
      } else if (resp.code === 302) {
        window.parent.window.location.href = "/login.html";
      }
    }
  });


})
