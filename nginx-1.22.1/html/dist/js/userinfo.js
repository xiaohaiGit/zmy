$(function () {

  var user = JSON.parse(localStorage.getItem("userinfo"));
  $("#email").val(user.email);
  $("#username").val(user.username);
  $("#age").val(user.age);
  $("#address").val(user.address);
  $("#phone").val(user.phone);
  $("#weixin").val(user.weixin);
  $("#qq").val(user.qq);

  $.validator.setDefaults({
    submitHandler: function () {
      // alert("Form successful submitted!");

      var user = {};
      user.email = $("#email").val();

      var username = $("#username").val();
      if (username.length != 0) {
        user.username = username;
      }

      var age = $("#age").val();
      if (age.length != 0) {
        user.age = age;
      }

      var address = $("#address").val();
      if (address.length != 0) {
        user.address = address;
      }

      var phone = $("#phone").val();
      if (phone.length != 0) {
        user.phone = phone;
      }

      var weixin = $("#weixin").val();
      if (weixin.length != 0) {
        user.weixin = weixin;
      }

      var qq = $("#qq").val();
      if (qq.length != 0) {
        user.qq = qq;
      }


      var token = localStorage.getItem("token");
      if (token == undefined || token == null || token.length === 0) {
        window.parent.window.location.href = "/login.html";
      }

      $.ajax({
        method: 'PUT',
        url: "/zmy/user",
        contentType: 'application/json',
        processData: false,
        headers: {
          token: token
        },
        data: JSON.stringify(user),
        success: function (resp) {
          if (resp.code == 200) {
            // window.location.href = "/login.html"
            var userCache = JSON.parse(localStorage.getItem("userinfo"));
            for (var key in user) {
              userCache[key] = user[key];
            }
            window.parent.localStorage.setItem("userinfo", JSON.stringify(userCache));
            alert("change userinfo success");
          } else if (resp.code == 302) {
            window.parent.window.location.href = "/login.html";
          } else {
            alert("change userinfo failed , desc : " + resp.desc);
          }
        }
      });

    }
  });
  $('#quickForm').validate({
    rules: {
      email: {
        required: true,
        email: true,
      },
      username: {
        required: true,
        minlength: 1
      },
      terms: {
        required: true
      },
    },
    messages: {
      email: {
        required: "Please enter a email address",
        email: "Please enter a valid email address"
      },
      password: {
        required: "Please provide a password",
        minlength: "Your password must be at least 5 characters long"
      },
      terms: "Please accept our terms"
    },
    errorElement: 'span',
    errorPlacement: function (error, element) {
      error.addClass('invalid-feedback');
      element.closest('.form-group').append(error);
    },
    highlight: function (element, errorClass, validClass) {
      $(element).addClass('is-invalid');
    },
    unhighlight: function (element, errorClass, validClass) {
      $(element).removeClass('is-invalid');
    }
  });
});
