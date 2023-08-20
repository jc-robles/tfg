const accelerometerGraphic = ['Accelerometer X', 'Accelerometer Y', 'Accelerometer Z']
const gyroscopeGraphic = ['Gyroscope X', 'Gyroscope Y','Gyroscope Z']
const quaternionGraphic = ['Quaternion W', 'Quaternion X', 'Quaternion Y','Quaternion Z']

function resetSendSplit() {
    $('#inputNameSplit').removeClass("is-invalid")
    $('#inputStartSplit').removeClass("is-invalid")
    $('#inputEndSplit').removeClass("is-invalid")

    $('#invalidFeedbackEmptyNameTest').hide()
    $('#invalidFeedbackDuplicateTest').hide()

    $('#invalidFeedbackNegativeStartTest').hide()
    $('#invalidFeedbackEmptyStartTest').hide()

    $('#invalidFeedbackEmptyEndTest').hide()
    $('#invalidFeedbackNegativeEndTest').hide()
    $('#invalidFeedbackGreaterThanEndTest').hide()
}

function checkNameErrorSendSplit() {
    let name = $('#inputNameSplit').val()
    if (name) {
        if ($('#listTest #' + name).length > 0) {
            $('#invalidFeedbackDuplicateTest').show()
            $('#invalidFeedbackEmptyNameTest').hide()
            $('#inputNameSplit').addClass("is-invalid")
            return true
        }
        return false
    }

    $('#invalidFeedbackEmptyNameTest').show()
    $('#invalidFeedbackDuplicateTest').hide()
    $('#inputNameSplit').addClass("is-invalid")
    return true
}

function checkStartErrorSendSplit() {
    let start = $('#inputStartSplit').val()
    if (start == '') {
        $('#invalidFeedbackNegativeStartTest').hide()
        $('#invalidFeedbackEmptyStartTest').show()
        $('#inputStartSplit').addClass("is-invalid")
        return true
    } else if (parseInt(start) < 0 ) {
        $('#invalidFeedbackNegativeStartTest').show()
        $('#invalidFeedbackEmptyStartTest').hide()
        $('#inputStartSplit').addClass("is-invalid")
        return true
    }
    return false
}

function checkEndErrorSendSplit() {
    let end = $('#inputEndSplit').val()
    let start = $('#inputStartSplit').val()
    if (end == '') {
        $('#invalidFeedbackEmptyEndTest').show()
        $('#invalidFeedbackNegativeEndTest').hide()
        $('#invalidFeedbackGreaterThanEndTest').hide()
        $('#inputEndSplit').addClass("is-invalid")
        return true
    } else if (parseInt(end)<=parseInt(start)) {
        $('#invalidFeedbackEmptyEndTest').hide()
        $('#invalidFeedbackNegativeEndTest').hide()
        $('#invalidFeedbackGreaterThanEndTest').show()
        $('#inputEndSplit').addClass("is-invalid")
         return true
    } else if (parseInt(end)<1) {
        $('#invalidFeedbackEmptyEndTest').hide()
        $('#invalidFeedbackNegativeEndTest').show()
        $('#invalidFeedbackGreaterThanEndTest').hide()
        $('#inputEndSplit').addClass("is-invalid")
         return true
    }
    return false
}

function resetInputsSendSplit() {
    $('#inputNameSplit').val("")
    $('#inputStartSplit').val("")
    $('#inputEndSplit').val("")
}

function showSuccessToastSendSplit() {
    $("#liveToast1 .toast-body").empty()
    $("#liveToast1 .toast-body").append("The " + name + " test was created successfully.")
    $("#splitTestSuccess").click()
}

function sendSplit() {
    resetSendSplit()
    let checkName = checkNameErrorSendSplit();
    let checkStart = checkStartErrorSendSplit();
    let checkEnd = checkEndErrorSendSplit();

    if (!checkName && !checkStart && !checkEnd) {
        generateHtmlSplit()
        generateGraphicsSplit()
        resetInputsSendSplit()
        showSuccessToastSendSplit()
    }
}

function addTabInMainTable(name) {
    $('#listTest').append('<li class="nav-item"><a class="nav-link text-body" id="' + name + '" onclick="selectTest(\'' + name + '\')">' + name + '</a></li>')
}

function generateHtmlSplit() {
    let name = $('#inputNameSplit').val()
    addTabInMainTable(name)
    $.ajax({
        url: '/generate-split-test?idTest=' + name,
        type: 'GET',
        data: new FormData(),
        processData: false,
        contentType: false,
        success: function(data) {
            $('#mainTable').append(data)
            $("#" + name + "Spinner").show()
        },
        async: false
    })
}

function generateGraphicsSplit() {
    let start = $('#inputStartSplit').val()
    let end = $('#inputEndSplit').val()
    let name = $('#inputNameSplit').val()

    let form = new FormData()
    form.append('start', parseInt(start))
    form.append('end', parseInt(end))
    form.append('fileName', name)
    createGraphics('/split-file', form, name)
}

function selectTest(idTest) {
    $("#listTest li a").each(function(i) {
       $(this).removeClass('active')
    })
    $("#"+idTest).addClass("active")
    $('div[id$="TableGraphic"]').hide()
    $('#' + idTest +'TableGraphic').show()
}

function deleteTest(idTest) {
    $("#"+idTest).parent().remove()
    $("#" + idTest + "TableGraphic").remove()
    $("#mainTest").click()

    $.ajax({
        url: '/delete-file?fileName=' + idTest,
        type: 'DELETE',
        data: new FormData(),
        processData: false,
        contentType: false,
        success: function(data) {
        },
        async: false
    })
}

function deleteAllTest() {
    $("#mainTable").remove()
    $.ajax({
        url: '/delete-all-file',
        type: 'DELETE',
        data: new FormData(),
        processData: false,
        contentType: false,
        success: function(data) {
            $('#bodyMainPageId').show()
        },
        async: false
    })
}

function processTest(nameTest) {
    $("#" + nameTest + "ProcessTestButton").children().attr('disabled', 'disabled')
    $("#" + nameTest + "testTypeSelectId").attr('disabled', 'disabled')
    let name = nameTest + "Processed"
    let testTypeName = $("#" + nameTest + "testTypeSelectId").val()

    if ($("#" + nameTest + "TableGraphic").children().length > 2) {
        $("#" + nameTest + "TableGraphic").children().last().remove()
    }

    generateHtmlProcessedTest(nameTest, testTypeName)
    generateGraphicsProcessedTest(nameTest, name, testTypeName)
}

function generateHtmlProcessedTest(nameTest, testTypeName) {
    let name = nameTest + "Processed"
    $.ajax({
        url: '/generate-process-test?idTest=' + name + "&testTypeName=" + testTypeName,
        type: 'GET',
        data: new FormData(),
        processData: false,
        contentType: false,
        success: function(data) {
            $('#' + nameTest + 'TableGraphic').append(data)
        },
        async: false
    })
}

function generateGraphicsProcessedTest(nameTest, nameTestProcessed, testTypeName) {
    let form = new FormData()
    form.append('fileName', nameTestProcessed)
    form.append('testTypeName', testTypeName)
    generateOutput('/process-test', form, nameTest, nameTestProcessed)
}

function download(id) {
    const xhr = new XMLHttpRequest()
    xhr.open('GET', '/download-processed-test?nameTest=' + id + 'Processed', true)
    xhr.onload = function() {
        if (xhr.status === 200) {
            const fileContent = xhr.response
            const url = URL.createObjectURL(new Blob([fileContent]))
            const link = document.createElement("a")
            link.href = url
            link.download = id + 'Processed.json'
            link.click()
            URL.revokeObjectURL(url)
        }
    }
    xhr.send()
}

function downloadRawTest(id) {
    const xhr = new XMLHttpRequest()
    xhr.open('GET', '/download-raw-test?nameTest=' + id, true)
    xhr.onload = function() {
        if (xhr.status === 200) {
            const fileContent = xhr.response
            const url = URL.createObjectURL(new Blob([fileContent]))
            const link = document.createElement("a")
            link.href = url
            link.download = id + '.csv'
            link.click()
            URL.revokeObjectURL(url)
        }
    }
    xhr.send()
}

function generateOutput(url, form, nameTest, nameTestProcessed) {
    $.ajax({
        url: url,
        type: 'POST',
        data: form,
        processData: false,
        contentType: false,
        success: function(data) {
            $("#" + nameTestProcessed + "Spinner").hide()
            if (Object.keys(data.alphanumericDataList).length != 0){
                createAlphanumeric(nameTestProcessed, data.alphanumericDataList)
            }
            if (Object.keys(data.arrayDataList).length != 0){
                let result = data.arrayDataList.reduce(function (r, a) {
                    r[a.graph] = r[a.graph] || []
                    r[a.graph].push(a)
                    return r
                }, Object.create(null))

                for (let category in result) {
                    let names = []
                    let values = []
                    result[category].forEach(item => {
                        names.push(item.name)
                        values.push(item.value)
                    })
                    newGraphic(nameTestProcessed + category + 'ProcessedGraphic',names,values)
                }

                $("#" + nameTestProcessed + "AccordionPanelsStayOpen").show()
            }

            $("#" + nameTest + "ProcessTestButton").hide()
            $("#" + nameTest + "DownloadTestButton").show()
        },
        error: function(xhr, ajaxOptions, thrownError) {
            $("#" + nameTestProcessed + "Spinner").hide()
            $("#" + nameTestProcessed + "ErrorProcessDataId").show()
            $("#" + nameTest + "ProcessTestButton").children().removeAttr('disabled')
            $("#" + nameTest + "testTypeSelectId").removeAttr('disabled')
        }
    })
}

function createAlphanumeric(name, alphanumericDataList) {
    alphanumericDataList.forEach((element) => {
        let data =
        "<div class='col-lg-3 mb-2'>" +
            "<ul class='list-group list-group-horizontal'>" +
                "<li class='list-group-item bg-body-tertiary'>" + element.name + "</li>" +
                "<li class='list-group-item'>" + element.value + "</li>" +
            "</ul>" +
        "</div>"
        $("#" + name + "AlphanumericDataId").append(data)
    })
    $("#" + name + "AlphanumericDataId").show()
}

function createGraphics(url, form, name) {
    $.ajax({
        url: url,
        type: 'POST',
        data: form,
        processData: false,
        contentType: false,
        success: function(data) {
            newGraphic(name + 'AccelerometerGraphic',accelerometerGraphic ,[data.accelerometerX, data.accelerometerY, data.accelerometerZ])
            newGraphic(name + 'GyroscopeGraphic',gyroscopeGraphic ,[data.gyroscopeX, data.gyroscopeY, data.gyroscopeZ])
            newGraphic(name + 'QuaternionGraphic',quaternionGraphic ,[data.quaternionW, data.quaternionX, data.quaternionY, data.quaternionZ])
            $("#" + name + "Spinner").hide()
            $("#" + name + "AccordionPanelsStayOpen").show()
        }
    })
}

function uploadTest() {
    let isError = checkErrorUploadTest()
    if (!isError) {
        clearMainTable()
        clearAllGraphics()
        hideMainBodyText()
        closeUploadModal()
        generateMainTest()
        setAllDataMainTest()
        loadPopover()
        loadToast()
    }
}

function clearMainTable() {
    $('#mainTable').remove()
}

function checkErrorUploadTest() {
    $('#invalidFeedbackFileRawId').hide()
    $('#uploadTestFileInput').removeClass("is-invalid")

    if($('#uploadTestFileInput').val() === '') {
        $('#uploadTestFileInput').addClass("is-invalid")
        $('#invalidFeedbackFileRawId').show()
        return true
    }

    return false
}

function hideMainBodyText() {
    $('#bodyMainPageId').hide()
}

function closeUploadModal() {
    $("#closeUploadModal").click()
}

function getUploadTestFile() {
    return document.getElementById("uploadTestFileInput").files[0]
}

function createFormDataWithDatasetFile(file) {
    let fileName = file.name
    let formData = new FormData()
    formData.append('file', file)
    return formData
}

function setAllDataMainTest() {
    let file = getUploadTestFile()
    $.ajax({
        url: '/upload-file',
        type: 'POST',
        data: createFormDataWithDatasetFile(file),
        processData: false,
        contentType: false,
        success: function(data) {
            setMainGraphics(data)
            setUploadedFileInfo(file.name)
            clearInputUploadModal()
            showAccordionMainData()
            enabledSplitTestButton()
        },
        error: function(xhr, ajaxOptions, thrownError) {
            showErrorUploadModal()
        }
    })
}

function showAccordionMainData() {
    $("#spinner").hide()
    $("#accordionPanelsStayOpenMainTest").show()
}

function setMainGraphics(data) {
    newGraphic('accelerometerGraphic',accelerometerGraphic ,[data.accelerometerX, data.accelerometerY, data.accelerometerZ])
    newGraphic('gyroscopeGraphic',gyroscopeGraphic ,[data.gyroscopeX, data.gyroscopeY, data.gyroscopeZ])
    newGraphic('quaternionGraphic',quaternionGraphic ,[data.quaternionW, data.quaternionX, data.quaternionY, data.quaternionZ])
}

function setUploadedFileInfo(fileName) {
    $('#inputUploadTestNameId').val(fileName)
}

function enabledSplitTestButton() {
    $("#splitTest").removeAttr('disabled')
}

function clearInputUploadModal() {
    $('#uploadTestFileInput').val("")
}

function showErrorUploadModal() {
   $("#spinner").hide()
   $("#mainTestErrorProcessDataId").show()
}

function generateMainTest() {
    $.ajax({
        url: '/generate-main-test',
        type: 'GET',
        processData: false,
        contentType: false,
        success: function(data) {
            $("body").append(data)
        },
        async: false
    })
}

function getAllTestType() {
    $.ajax({
        url: '/test-type/all-test-type',
        type: 'GET',
        processData: false,
        contentType: false,
        success: function(data) {
            $('#removeTestTypeSelectId').find('option').remove()
            data.forEach((element, index, array) => {
            if (index==0) {
                $("#removeTestTypeSelectId").append('<option value="' + element + '" selected>' + element + '</option>')
            } else {
                $("#removeTestTypeSelectId").append('<option value="' + element + '">' + element + '</option>')
            }
            })
        }
    })
}