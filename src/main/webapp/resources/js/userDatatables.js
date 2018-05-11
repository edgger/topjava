var ajaxUrl = "ajax/admin/users/";
var datatableApi;

// $(document).ready(function () {
$(function () {
    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "name"
            },
            {
                "data": "email"
            },
            {
                "data": "roles"
            },
            {
                "data": "enabled"
            },
            {
                "data": "registered"
            },
            {
                "defaultContent": "Edit",
                "orderable": false
            },
            {
                "defaultContent": "Delete",
                "orderable": false
            }
        ],
        "order": [
            [
                0,
                "asc"
            ]
        ]
    });
    makeEditable();
    $(".enableChange").click(function () {
        var enabled = $(this).is(":checked");
        var thisRow = $(this).closest("tr");
        if (enabled) {
            thisRow.removeClass("disabledUser");
        } else {
            thisRow.addClass("disabledUser");
        }
        changeEnabled(thisRow.attr("id"), enabled);
    });
});

function changeEnabled(id, enabled) {
    $.ajax({
        url: ajaxUrl + id + "?enabled=" + enabled,
        type: "PUT",
        success: function () {
            updateTable();
        }
    });
}