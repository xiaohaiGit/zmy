function search() {

  var token = localStorage.getItem("token");
  if (token == undefined || token == null || token.length == 0) {
    window.parent.window.location.href = "/login.html";
  }

  var number = $("#search-text").val();
  if (!number.match(/[0-9]{6}-[0-9]{3}/)) {
    alert("device number is error , please enter right device number");
    return;
  }

  $.ajax({
    method: 'GET',
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

        var air = resp.data;

        $("#region").val(air.region).trigger("change");
        $("#type").val(air.type).trigger("change");
        $("#number").val(air.number);
        $("#building").val(air.building).trigger("change");
        $("#storey").val(air.storey).trigger("change");
        $("#desc").val(air.desc);

        // alert("search device : " + number + " success");

      } else if (resp.code == 302) {
        window.parent.window.location.href = "/login.html";
      } else {
        alert("search device : " + number + " failed , desc: " + resp.desc);
      }
    }
  });
}


$(function () {
  //Initialize Select2 Elements
  $('.select2').select2()

  //Initialize Select2 Elements
  $('.select2bs4').select2({
    theme: 'bootstrap4'
  })

  //Money Euro
  $('[data-mask]').inputmask()


  $("#search").click(search);

  $("#search-text").keypress(function (event) {
    if (event.keyCode === 13) {
      search();
    }
  });


  $("#modify-air").click(function () {
    var token = localStorage.getItem("token");
    if (token == undefined || token == null || token.length == 0) {
      window.parent.window.location.href = "/login.html";
    }

    var region = $("#region").val();
    var type = $("#type").val();
    var number = $("#number").val();
    var building = $("#building").val();
    var storey = $("#storey").val();
    var desc = $("#desc").val();


    if (region == undefined || region == null || region.length == 0) {
      alert("区域未选择");
      return;
    }

    if (type == undefined || type == null || type.length == 0) {
      alert("类型未选择");
      return;
    }

    if (building == undefined || building == null || building.length == 0) {
      alert("楼栋未选择");
      return;
    }

    if (storey == undefined || storey == null || storey.length == 0) {
      alert("楼层未选择");
      return;
    }


    if (!number.match(/[0-9]{6}-[0-9]{3}/)) {
      alert("设备编码格式不正确");
      return;
    }

    if (desc == undefined || desc == null || desc.length == 0) {
      alert("必须添加描述");
      return;
    }

    var obj = {
      region: region,
      type: type,
      number: number,
      building: building,
      storey: storey,
      desc: desc
    }

    $.ajax({
      method: 'PUT',
      url: "/zmy/air",
      contentType: 'application/json',
      headers: {
        token: token
      },
      processData: false,
      data: JSON.stringify(obj),
      success: function (resp) {
        if (resp.code == 200) {
          // window.location.href = "/login.html"

          alert("modify device : " + number + " success");
        } else if (resp.code == 302) {
          window.parent.window.location.href = "/login.html";
        } else {
          alert("modify device : " + number + " failed , desc: " + resp.desc);
        }
      }
    });


  });


});
