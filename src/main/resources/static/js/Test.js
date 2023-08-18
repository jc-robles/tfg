
function sendSplit() {
    /* validate */
    let checkName = false
    let checkStart = false
    let checkEnd = false

    let start = $('#inputStartSplit').val()
    let end = $('#inputEndSplit').val()
    let name = $('#inputNameSplit').val()

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

    if (name) {
        checkName = true
        if ($('#listTest #' + name).length > 0) {
            $('#invalidFeedbackDuplicateTest').show()
            $('#invalidFeedbackEmptyNameTest').hide()
            checkName = false
        }
    } else {
        $('#invalidFeedbackEmptyNameTest').show()
        $('#invalidFeedbackDuplicateTest').hide()
    }

    if (start < 0 ) {
        $('#invalidFeedbackNegativeStartTest').show()
        $('#invalidFeedbackEmptyStartTest').hide()
    } else if (start == '') {
        $('#invalidFeedbackNegativeStartTest').hide()
        $('#invalidFeedbackEmptyStartTest').show()
    } else {
        checkStart = true
    }

    if (end == '') {
        $('#invalidFeedbackEmptyEndTest').show()
        $('#invalidFeedbackNegativeEndTest').hide()
        $('#invalidFeedbackGreaterThanEndTest').hide()
    } else if (end<=start) {
        $('#invalidFeedbackEmptyEndTest').hide()
        $('#invalidFeedbackNegativeEndTest').hide()
        $('#invalidFeedbackGreaterThanEndTest').show()
    } else if (end<1) {
        $('#invalidFeedbackEmptyEndTest').hide()
        $('#invalidFeedbackNegativeEndTest').show()
        $('#invalidFeedbackGreaterThanEndTest').hide()
    } else {
        checkEnd = true
    }

    if (checkName && checkStart && checkEnd) {
        $('#inputNameSplit').val("")
        $('#inputStartSplit').val("")
        $('#inputEndSplit').val("")

        generateHtmlSplit(name)
        generateGraphicsSplit(name, start, end)

        $("#splitTestSuccess").click()
        $("#liveToast1 .toast-body").empty()
        $("#liveToast1 .toast-body").append("The " + name + " test was created successfully.")
    } else {
        if (!checkName) {
            $('#inputNameSplit').addClass("is-invalid")
        }
        if (!checkStart) {
            $('#inputStartSplit').addClass("is-invalid")
        }
        if (!checkEnd) {
            $('#inputEndSplit').addClass("is-invalid")
        }
    }
}

function generateHtmlSplit(name) {
    $('#listTest').append('<li class="nav-item"><a class="nav-link text-body" id="' + name + '" onclick="selectTest(\'' + name + '\')">' + name + '</a></li>')
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

function generateGraphicsSplit(name, start, end) {
    let f = $('#formSplitId')[0]
    f.start.value = start
    f.end.value = end
    f.fileName.value = name
    createGraphics('/split-file', new FormData(f), name)
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

                reload()
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
            newGraphic(name + 'AccelerometerGraphic',['Accelerometer X', 'Accelerometer Y','Accelerometer Z' ] ,[data.accelerometerX, data.accelerometerY, data.accelerometerZ])
            newGraphic(name + 'GyroscopeGraphic',['Gyroscope X', 'Gyroscope Y','Gyroscope Z'] ,[data.gyroscopeX, data.gyroscopeY, data.gyroscopeZ])
            newGraphic(name + 'QuaternionGraphic',['Quaternion W', 'Quaternion X', 'Quaternion Y','Quaternion Z'] ,[data.quaternionW, data.quaternionX, data.quaternionY, data.quaternionZ])
            reload()
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
        showMainTableLoading()
        closeUploadModal()
        generateMainTest()
        setAllDataMainTest()
        popover()
    }
}

function clearMainTable() {
    $('#mainTable').remove()
}

function checkErrorUploadTest() {
    $('#invalidFeedbackFileRawId').hide()
    let isError = false
    if($('#uploadTestFileInput').val() === '') {
        $('#uploadTestModal .modal-body').addClass("was-validated")
        $('#invalidFeedbackFileRawId').show()
        isError = true
    } else {
        $('#uploadTestModal .modal-body').removeClass("was-validated")
    }
    return isError
}

function hideMainBodyText() {
    $('#bodyMainPageId').hide()
}

function showMainTableLoading() {
    $("#mainTable").show()
    $("#spinner").show()
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
            showAccordionMainData()
            setMainGraphics(data)
            setUploadedFileInfo(file.name)
            clearInputUploadModal()
        },
        error: function(xhr, ajaxOptions, thrownError) {
            showErrorUploadModal()
        }
    })
}

function showAccordionMainData() {
    $("#spinner").hide()
    $("#accordionPanelsStayOpenExample").show()
}

function setMainGraphics(data) {
    newGraphic('accelerometerGraphic',['Accelerometer X', 'Accelerometer Y','Accelerometer Z' ] ,[data.accelerometerX, data.accelerometerY, data.accelerometerZ])
    newGraphic('gyroscopeGraphic',['Gyroscope X', 'Gyroscope Y','Gyroscope Z'] ,[data.gyroscopeX, data.gyroscopeY, data.gyroscopeZ])
    newGraphic('quaternionGraphic',['Quaternion W', 'Quaternion X', 'Quaternion Y','Quaternion Z'] ,[data.quaternionW, data.quaternionX, data.quaternionY, data.quaternionZ])
}

function setUploadedFileInfo(fileName) {
    $('#inputUploadTestNameId').val(fileName)
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
            $("#splitTest").removeAttr('disabled')
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