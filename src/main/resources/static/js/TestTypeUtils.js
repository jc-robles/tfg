function addOutputData() {
    let outputDataValue = $('#outputData').val();
    let isError = false;

    $('#invalidFeedbackNameFieldId').hide()
    $('#invalidFeedbackFieldId').hide()

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
                popover();
            }
        });
    }
}

function addGraphic() {
    $('#invalidFeedbackGraphicName').hide();
    $("#grouping").removeClass("is-invalid")
    let groupingValue = $('#grouping').val();
    let isError = false
    if (groupingValue == ''){
        isError = true
        $('#invalidFeedbackGraphicName').show();
        $("#grouping").addClass("is-invalid")
    }

    if (!isError){
        $('#grouping').val('');
        $.ajax({
            url: '/test-type/create/graph?groupingName=' + groupingValue,
            type: 'GET',
            processData: false,
            contentType: false,
            success: function(data) {
                $("#graphicRow").append(data);
                $("[id$='SelectDataNameId'] select").append('<option value="' + groupingValue + '">' + groupingValue + '</option>');
            }
        });
    }
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
    resetValuesCreateTestType()
    let isError = checkErrorCreateTestType();
    if (!isError) {
        let createTestDTO = generateTestTypeDTO()
        sendTestTypeDTO(createTestDTO);
    }
}

function resetValuesCreateTestType() {
    $("#outputData").val("")
    $('#invalidFeedbackNameFieldId').hide()
    $('#invalidFeedbackFieldId').hide()
    $('#invalidFeedbackNameTestId').hide()
    $('#invalidFeedbackFiledId').hide()
    $('#invalidFeedbackEmptyGraphic').hide()
    $("[id$='InvalidFeedbackEmptyGraphic']").each(function() {
        $(this).hide()
    })
}

function checkErrorCreateTestType() {
    let isError = false;
    if($('#createTestName').val() === '') {
        $('#createTestNameLiId').addClass("was-validated")
        $('#invalidFeedbackNameTestId').show()
        isError = true;
    } else {
        $('#createTestNameLiId').removeClass("was-validated")
    }
    if($('#createTestFileInput').val() === '') {
        $('#fileCreateTestLiId').addClass("was-validated")
        $('#invalidFeedbackFiledId').show()
        isError = true;
    } else {
        $('#fileCreateTestLiId').removeClass("was-validated")
    }
    if($('#outputDataRow').children().length <= 1) {
        $('#invalidFeedbackFieldId').show()
        $('#outputDataRow').children(":first").addClass("was-validated")
        isError = true;
    } else {
        $('#outputDataRow').children(":first").removeClass("was-validated")
    }

    $("#outputDataRow .toast-body").each(function() {
        if ($('input[name=' + $(this).attr('name') + 'RadioButtonName]:checked').val() === 'DATA_ARRAY') {
            $("[id$='SelectDataNameId'] select").each(function() {
                if(!$(this).val()) {
                    let parentId = $(this).parent().attr('id')
                    let id = parentId.split("SelectDataNameId")[0]
                    $('#'+ id + 'InvalidFeedbackEmptyGraphic').show()
                    $('#outputDataRow').children(":first").addClass("was-validated")
                }
            });
        }
    });

    return isError;
}

function getAllFieldsCreateTestType() {
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
    return fields;
}

function generateTestTypeDTO() {
    let fields = getAllFieldsCreateTestType();
    let CreateTestDTO = {
        nameTest: $('#createTestName').val(),
        fields: fields
    }
    return CreateTestDTO;
}

function sendTestTypeDTO(createTestDTO) {
    var file = document.getElementById("createTestFileInput").files[0];
    var formData = new FormData();
    formData.append('createTest', JSON.stringify(createTestDTO))
    formData.append('fileTest', file)
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

function deleteTestType() {
    let testType = $("#removeTestTypeSelectId").val();
    $.ajax({
        url: '/test-type/delete?testType=' + testType,
        type: 'DELETE',
        processData: false,
        contentType: false,
        success: function(data) {
            $("#closeDeleteTestTypeModal").click()
            $("#removeTestTypeSelectId option[value='" + testType + "']").each(function() {
                $(this).remove();
            });

            $("[id$='testTypeSelectId'] option[value='" + testType + "']").each(function() {
                $(this).remove();
            });
        }
    });
}