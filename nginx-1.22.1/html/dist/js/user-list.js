$(function () {
  $("#example1").DataTable({
    "ajax": {
      "url": "/zmy/users",
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
        "data": "id",
        visible: false
      },
      {
        "data": "username"
      },
      {
        "data": "email"
      },
      {
        "data": "age"
      },
      {
        "data": "address"
      },
      {
        "data": "phone"
      },
      {
        "data": "weixin"
      },
      {
        "data": "qq"
      },
      {
        "data": "createTime",
      },
      {
        "data": "updateTime",
      },
    ]
  }).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');

});
