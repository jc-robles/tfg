function addOutputData() {
    resetAddOutputData()
    let isError = checkErrorAddOutputData()

    let outputDataValue = $('#outputData').val()
    if (!isError) {
        $('#outputData').val('')
        $.ajax({
            url: '/test-type/add-output-data?dataName=' + outputDataValue,
            type: 'GET',
            processData: false,
            contentType: false,
            success: function(data) {
                $("#outputDataRow").append(data)
                loadPopover()
            }
        })
    }
}

function resetAddOutputData() {
    $('#invalidFeedbackNameFieldId').hide()
    $('#invalidFeedbackFieldId').hide()
    $('#outputDataRow').children(":first").removeClass("was-validated")
}

function checkErrorAddOutputData() {
    let outputDataValue = $('#outputData').val()
    if (outputDataValue === '') {
        $('#invalidFeedbackNameFieldId').show()
        $('#outputDataRow').children(":first").addClass("was-validated")
        return true
    }
    return false
}

function addGraphic() {
    $('#invalidFeedbackGraphicName').hide()
    $("#graphId").removeClass("is-invalid")
    let graphValue = $('#graphId').val()
    let isError = false
    if (graphValue == ''){
        isError = true
        $('#invalidFeedbackGraphicName').show()
        $("#graphId").addClass("is-invalid")
    }

    if (!isError){
        $('#graphId').val('')
        $.ajax({
            url: '/test-type/create/graph?graphName=' + graphValue,
            type: 'GET',
            processData: false,
            contentType: false,
            success: function(data) {
                $("#graphicRow").append(data)
                $("[id$='SelectDataNameId'] select").append('<option value="' + graphValue + '">' + graphValue + '</option>')
            }
        })
    }
}

function deleteDataName(dataNameId) {
    $("#" + dataNameId).remove()
}

function deleteGraph(graphId, graphValue) {
    $.ajax({
        url: '/test-type/remove-graph?graphId=' + graphId,
        type: 'GET',
        processData: false,
        contentType: false,
        success: function(data) {
            $("#" + graphId).remove()
            $("[id$='SelectDataNameId'] option[value='" + graphValue + "']").each(function() {
                $(this).remove()
            })
        }
    })
}

function selectDisabled(selectId){
    $("#" + selectId).hide()
}

function selectEnabled(selectId){
    $("#" + selectId).show()
}

function restoreDefaultValues() {
    $("#createTestName").val("")
    $("#graphId").val("")
    $("#createTestFileInput").val("")
    $("#outputDataRow").children().each(function(index) {
        if(index>0){
            $(this).remove()
        }
    })

}

function createTest() {
    resetValuesCreateTestType()
    let isError = checkErrorCreateTestType()
    if (!isError) {
        let createTestDTO = generateTestTypeDTO()
        sendTestTypeDTO(createTestDTO)
    }
}

function resetValuesCreateTestType() {
    $("#outputData").val("")
    $('#invalidFeedbackNameFieldId').hide()
    $('#invalidFeedbackFieldId').hide()
    $('#invalidFeedbackNameTestId').hide()
    $('#invalidFeedbackFiledId').hide()
    $("[id$='InvalidFeedbackEmptyGraphic']").each(function() {
        $(this).hide()
    })
}

function checkErrorCreateTestType() {
    let isError = false
    if($('#createTestName').val() === '') {
        $('#createTestNameLiId').addClass("was-validated")
        $('#invalidFeedbackNameTestId').show()
        isError = true
    } else {
        $('#createTestNameLiId').removeClass("was-validated")
    }
    if($('#createTestFileInput').val() === '') {
        $('#fileCreateTestLiId').addClass("was-validated")
        $('#invalidFeedbackFiledId').show()
        isError = true
    } else {
        $('#fileCreateTestLiId').removeClass("was-validated")
    }
    if($('#outputDataRow').children().length <= 1) {
        $('#invalidFeedbackFieldId').show()
        $('#outputDataRow').children(":first").addClass("was-validated")
        isError = true
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
                    $(this).addClass("is-invalid")
                    isError = true
                }
            })
        }
    })

    return isError
}

function getAllFieldsCreateTestType() {
    let fields = []
    $("#outputDataRow .toast-body").each(function() {
        let name = $(this).attr('name')
        let data_type = $('input[name=' + $(this).attr('name') + 'RadioButtonName]:checked').val()
        let graph
        if ($('#' + $(this).attr('name') + 'SelectDataNameId').is(':visible')) {
            graph = $('#' + $(this).attr('name') + 'SelectDataNameId').val()
        }
        let field = {
            name: name,
            dataType: data_type,
            graph: graph
        }
        fields.push(field)
    })
    return fields
}

function generateTestTypeDTO() {
    let fields = getAllFieldsCreateTestType()
    let CreateTestDTO = {
        nameTest: $('#createTestName').val(),
        fields: fields
    }
    return CreateTestDTO
}

function sendTestTypeDTO(createTestDTO) {
    var file = document.getElementById("createTestFileInput").files[0]
    var formData = new FormData()
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
    })
}

function deleteTestType() {
    let testType = $("#removeTestTypeSelectId").val()
    $.ajax({
        url: '/test-type/delete?testType=' + testType,
        type: 'DELETE',
        processData: false,
        contentType: false,
        success: function(data) {
            $("#closeDeleteTestTypeModal").click()
            $("#removeTestTypeSelectId option[value='" + testType + "']").each(function() {
                $(this).remove()
            })

            $("[id$='testTypeSelectId'] option[value='" + testType + "']").each(function() {
                $(this).remove()
            })
        }
    })
}

function getAllGraphs() {
    $.ajax({
        url: '/test-type/all-graph',
        type: 'GET',
        processData: false,
        contentType: false,
        success: function(data) {
            $("#graphicRow").append(data)
        }
    })
}