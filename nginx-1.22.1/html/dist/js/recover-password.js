function GetQueryString(name) {
  var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
  var r = window.location.search.substr(1).match(reg);
  if (r != null) return unescape(r[2]);
  return null;
};


$(function () {
  $("#send-button").click(function () {
    var newPassword = $("#newPassword").val();
    var againPassword = $("#againPassword").val();
    if (newPassword != againPassword) {
      alert("两次密码不一致");
      return;
    }

    var forgotToken = GetQueryString("forgot-token");


    $.ajax({
      method: 'POST',
      url: "/zmy/recover-password",
      data: {
        forgotToken: forgotToken,
        password: newPassword
      },
      success: function (resp) {
        if (resp.code == 200) {
          alert("send mail success , please check your email");
          window.location.href = "http://localhost/login.html";
        } else {
          alert("recover password failed , desc : " + resp.desc);
        }
      }
    });

  });
});
