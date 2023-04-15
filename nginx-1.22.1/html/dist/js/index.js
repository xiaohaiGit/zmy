$(function () {


  var token = localStorage.getItem("token");
  if (token == undefined || token == null || token.length === 0) {
    window.location.href = "/login.html";
    return;
  }
  // 获取用户信息
  $.ajax({
    method: 'GET',
    url: "/zmy/user",
    // contentType: 'application/json',
    headers: {
      token: token
    },
    success: function (resp) {
      if (resp.code == 200) {
        var userinfo = JSON.stringify(resp.data);
        localStorage.setItem("userinfo", userinfo);
        var user = resp.data;

        $("#head-image").attr("src", user.headImagePath);
        $("#username").text(user.email);

      } else if (resp.code == 302) {
        window.location.href = "/login.html"
      } else {
        alert("get userinfo failed , desc : " + resp.desc);
      }
    },
    error: function (XMLHttpRequest, textStatus, errorThrown) {
      // alert(XMLHttpRequest.status);
      // alert(XMLHttpRequest.readyState);
      // alert(textStatus);
      console.log("request status : " + XMLHttpRequest.status);
      window.location.href = "/login.html"
    }
  });


  $("#logout").click(function () {
    var token = localStorage.getItem("token");
    if (token == undefined || token == null || token.length === 0) {
      window.location.href = "/login.html";
      return;
    }

    $.ajax({
      method: 'GET',
      url: "/zmy/logout",
      // contentType: 'application/json',
      headers: {
        token: token
      },
      success: function (resp) {
        if (resp.code == 200 || resp.code == 302) {
          localStorage.clear();
          window.location.href = "/login.html"
        } else {
          alert("logout failed , desc : " + resp.desc);
        }
      }
    });

  });


  // $("#dashboard-a")[0].click();

});
