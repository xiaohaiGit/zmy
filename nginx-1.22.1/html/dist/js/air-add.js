$(function () {
  //Initialize Select2 Elements
  $('.select2').select2()

  //Initialize Select2 Elements
  $('.select2bs4').select2({
    theme: 'bootstrap4'
  })

  //Money Euro
  $('[data-mask]').inputmask()

  $("#add-air").click(function () {
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
      alert("必须添加区域");
      return;
    }

    if (building == undefined || building == null || building.length == 0) {
      alert("必须添加楼栋");
      return;
    }

    if (storey == undefined || storey == null || storey.length == 0) {
      alert("必须添加楼层");
      return;
    }

    if (type == undefined || type == null || type.length == 0) {
      alert("必须添加类型");
      return;
    }


    if (number == "______-___" || number.length == 0) {
      alert("请输入设备编号");
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
      method: 'POST',
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


});
