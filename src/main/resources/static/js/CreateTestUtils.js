function addOutputData() {
    let outputDataValue = $('#outputData').val();
    let isError = false;

    $('#invalidFeedbackNameFieldId').hide()
    $('#invalidFeedbackFieldId').hide()
    $('#invalidFeedbackDataTypeId').hide()

    if (outputDataValue === '') {
        $('#invalidFeedbackNameFieldId').show()
        $('#outputDataRow').children(":first").addClass("was-validated")
        isError = true;
    } else {
        $('#outputDataRow').children(":first").removeClass("was-validated")
    }

    if (!isError) {
        $('#outputData').val('');
        $.ajax({
            url: '/test-type/add-output-data?dataName=' + outputDataValue,
            type: 'GET',
            processData: false,
            contentType: false,
            success: function(data) {
                $("#outputDataRow").append(data);
            }
        });
    }
}

function addGrouping() {
    let groupingValue = $('#grouping').val();
    $('#grouping').val('');
    $.ajax({
        url: '/test-type/add-grouping?groupingName=' + groupingValue,
        type: 'GET',
        processData: false,
        contentType: false,
        success: function(data) {
            $("#groupingRow").append(data);
            $("[id$='SelectDataNameId']").each(function(data, index) {
                console.log(data);
                console.log(index);
            });
            $("[id$='SelectDataNameId']").append('<option value="' + groupingValue + '">' + groupingValue + '</option>');
        }
    });
}

function deleteDataName(dataNameId) {
    $("#" + dataNameId).remove();
}

function deleteGrouping(groupingId, groupingValue) {
    $.ajax({
        url: '/test-type/remove-grouping?groupingId=' + groupingId,
        type: 'GET',
        processData: false,
        contentType: false,
        success: function(data) {
            $("#" + groupingId).remove();
            $("[id$='SelectDataNameId'] option[value='" + groupingValue + "']").each(function() {
                $(this).remove();
            });
        }
    });
}

function selectDisabled(selectId){
    $("#" + selectId).hide();
}

function selectEnabled(selectId){
    $("#" + selectId).show();
}

function restoreDefaultValues() {
    $("#createTestName").val("")
    $("#grouping").val("")
    $("#createTestFileInput").val("")
    $("#outputDataRow").children().each(function(index) {
        if(index>0){
            $(this).remove()
        }
    })

}

function createTest() {
    let isError = false;
    $("#outputData").val("")

    $('#invalidFeedbackNameFieldId').hide()
    $('#invalidFeedbackFieldId').hide()
    $('#invalidFeedbackDataTypeId').hide()
    if($('#createTestName').val() === '') {
        $('#createTestNameLiId').addClass("was-validated")
        isError = true;
    } else {
        $('#createTestNameLiId').removeClass("was-validated")
    }
    if($('#createTestFileInput').val() === '') {
        $('#fileCreateTestLiId').addClass("was-validated")
        isError = true;
    } else {
        $('#fileCreateTestLiId').removeClass("was-validated")
    }
    if($('#outputDataRow').children().length <= 1) {
        $('#invalidFeedbackFieldId').show()
        $('#invalidFeedbackDataTypeId').hide()
        $('#outputDataRow').children(":first").addClass("was-validated")
        isError = true;
    } else {
        $('#outputDataRow').children(":first").removeClass("was-validated")
    }
    $("#outputDataRow .toast-body").each(function() {
        console.log('val: ' + $('#' + $(this).attr('name') + 'SelectDataNameId').val())
        console.log('checked: ' + $('input[name=' + $(this).attr('name') + 'RadioButtonName]:checked').val())

        if ($('input[name=' + $(this).attr('name') + 'RadioButtonName]:checked').val() === 'DATA_ARRAY' && $('#' + $(this).attr('name') + 'SelectDataNameId').val() == null) {
           $('#invalidFeedbackFieldId').hide()
           $('#invalidFeedbackDataTypeId').show()
           $('#outputDataRow').children(":first").addClass("was-validated")
        }
    })

    if (!isError) {
        let fields = [];
        $("#outputDataRow .toast-body").each(function() {
            let name = $(this).attr('name');
            let data_type = $('input[name=' + $(this).attr('name') + 'RadioButtonName]:checked').val();
            let grouping;
            if ($('#' + $(this).attr('name') + 'SelectDataNameId').is(':visible')) {
                grouping = $('#' + $(this).attr('name') + 'SelectDataNameId').val();
            }
            let field = {
                name: name,
                dataType: data_type,
                grouping: grouping
            }
            fields.push(field);
        });

        let CreateTestDTO = {
            nameTest: $('#createTestName').val(),
            fields: fields
        }

        var file = document.getElementById("createTestFileInput").files[0];
        var formData = new FormData();
        formData.append('createTest', JSON.stringify(CreateTestDTO))
        formData.append('fileTest', file)

        console.log(JSON.stringify(CreateTestDTO))

        $.ajax({
            url: '/test-type/create',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(data) {
                $("#closeCreateTestId").click()
                restoreDefaultValues()
            }
        });
    }
}

function deleteTestType() {
    let testType = $("#removeTestTypeSelectId").val();
    $.ajax({
        url: '/test-type/delete?testType=' + testType,
        type: 'DELETE',
        processData: false,
        contentType: false,
        success: function(data) {
            $("closeDeleteTestTypeModal").click()
            $("#removeTestTypeSelectId option[value='" + testType + "']").each(function() {
                $(this).remove();
            });

            $("[id$='testTypeSelectId'] option[value='" + testType + "']").each(function() {
                $(this).remove();
            });
        }
    });
}