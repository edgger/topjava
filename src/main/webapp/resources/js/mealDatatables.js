var ajaxUrl = "ajax/meals/";
var datatableApi;

// $(document).ready(function () {
$(function () {
    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "dateTime"
            },
            {
                "data": "description"
            },
            {
                "data": "calories"
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
});

function filterMeals() {
    datatableApi.draw();
}

function resetFilterMeals() {
    var filterForm = $("#filterMeals");
    var filterInputs = filterForm.find("input");
    for (var i = 0; i<filterInputs.length; i++){
        filterInputs[i].value = "";
    }
    datatableApi.draw();
}

$.fn.dataTable.ext.search.push(
    function (settings, data, dataIndex) {
        var filterForm = $("#filterMeals");
        var filterStartDate = normalizeDate(new Date(filterForm.find("#startDate").val()));
        var filterEndDate = normalizeDate(new Date(filterForm.find("#endDate").val()));
        var filterStartTime = normalizeTime(filterForm.find("#startTime").val());
        var filterEndTime = normalizeTime(filterForm.find("#endTime").val());
        var rowDate = normalizeDate(new Date(data[0]));
        var rowTime = normalizeTime(data[0].split(' ')[1]);
        if (rowTime === undefined) {
            rowTime = normalizeTime(data[0].split('T')[1]);
        }
        if ((isNaN(filterStartDate) || filterStartDate <= rowDate) && (isNaN(filterEndDate) || filterEndDate > rowDate)
            && (isNaN(filterStartTime) || filterStartTime <= rowTime) && (isNaN(filterEndTime) || filterEndTime > rowTime)) {
            return true;
        } else {
            return false;
        }
    }
);

var normalizeDate = function (date) {
    return date.getFullYear() + '' + (("0" + (date.getMonth() + 1)).slice(-2)) + '' + ("0" + date.getDate()).slice(-2);
};

var normalizeTime = function (timeString) {
    if (timeString !== undefined) {
        var timeNums = timeString.split(":");
        return timeNums[0] + '' + timeNums[1];
    }
};