
function sendSplit() {
    /* validate */
    let isValid = false;
    let start = $('#inputStartSplit').val();
    let end = $('#inputEndSplit').val();
    let name = $('#inputNameSplit').val();

    if (start >=0 && start<end && name) {
        isValid = true;
    }

    if (isValid) {
        generateHtmlSplit(name);
        generateGraphicsSplit();
    } else {
        $('#inputSplitTest').addClass("was-validated")
    }
}

function generateHtmlSplit(name) {
    $('#listTest').append('<li class="nav-item"><a class="nav-link text-body" id="' + name + '" onclick="selectTest(\'' + name + '\')">' + name + '</a></li>');
    $.ajax({
        url: '/generate-split-test?idTest=' + name,
        type: 'GET',
        data: new FormData(),
        processData: false,
        contentType: false,
        success: function(data) {
            $('#mainTable').append(data);
            $("#" + name + "Spinner").show();
        },
        async: false
    });
}

function generateGraphicsSplit() {
    let name = $('#inputNameSplit').val();
    let f = $('#formSplitId')[0];
    f.start.value = $('#inputStartSplit').val();
    f.end.value = $('#inputEndSplit').val();
    f.fileName.value = name;
    createGraphics('/split-file', new FormData(f), name);
}

function selectTest(idTest) {
    $("#listTest li a").each(function(i) {
       $(this).removeClass('active');
    });
    $("#"+idTest).addClass("active");
    $('div[id$="TableGraphic"]').hide();
    $('#' + idTest +'TableGraphic').show();
}

function deleteTest(idTest) {
    $("#"+idTest).parent().remove();
    $("#" + idTest + "TableGraphic").remove();
    $("#mainTest").click();

    $.ajax({
        url: '/delete-file?fileName=' + idTest,
        type: 'DELETE',
        data: new FormData(),
        processData: false,
        contentType: false,
        success: function(data) {
        },
        async: false
    });
}

function deleteAllTest(idTest) {
    $("#mainTable").remove();
    $.ajax({
        url: '/delete-all-file',
        type: 'DELETE',
        data: new FormData(),
        processData: false,
        contentType: false,
        success: function(data) {
        },
        async: false
    });
}

function processTest(nameTest) {
    let name = nameTest + "Processed";
    let testTypeName = $("#" + nameTest + "testTypeSelectId").val();
    generateHtmlProcessedTest(nameTest);
    generateGraphicsProcessedTest(name, testTypeName);
    $("#" + nameTest + "DownloadTestButton").show();
}

function generateHtmlProcessedTest(nameTest) {
    let name = nameTest + "Processed"
    $.ajax({
        url: '/generate-process-test?idTest=' + name,
        type: 'GET',
        data: new FormData(),
        processData: false,
        contentType: false,
        success: function(data) {
            $('#' + nameTest + 'TableGraphic').append(data);
            $("#" + name + "Spinner").show();
        },
        async: false
    });
}

function generateGraphicsProcessedTest(nameTest, testTypeName) {
    let form = new FormData()
    form.append('fileName', nameTest)
    form.append('testTypeName', testTypeName)
    generateOutput('/process-test', form, nameTest);
    /*createGraphics('/process-test', form, nameTest);*/
}

function download(id) {
      var timestamp = new Date().getTime();
      var url = '/tests-processed/' + id + 'Processed.csv' + '?t=' + timestamp;
      var link = document.createElement('a');
      link.href = url;
      link.download = id + 'Processed.csv';
      link.click();
};

function generateOutput(url, form, name) {
    $.ajax({
        url: url,
        type: 'POST',
        data: form,
        processData: false,
        contentType: false,
        success: function(data) {
            $("#" + name + "Spinner").hide();
            if (Object.keys(data.alphanumericDataList).length != 0){
                createAlphanumeric(name, data.alphanumericDataList)
            }
            if (Object.keys(data.arrayDataList).length != 0){
                console.log('arrayDataList empty')

                result = data.arrayDataList.reduce(function (r, a) {
                    r[a.group] = r[a.group] || [];
                    r[a.group].push(a);
                    return r;
                }, Object.create(null));

                newGraphic(name + 'AccelerometerGraphic',
                    ['Accelerometer X', 'Accelerometer Y','Accelerometer Z' ],
                    [data.accelerometerX, data.accelerometerY, data.accelerometerZ]);
                $("#" + name + "AccordionPanelsStayOpen").show();
            }
        },
        async: false
    });
}

function createAlphanumeric(name, alphanumericDataList) {
    alphanumericDataList.forEach((element) => {
        let data =
        "<div class='col-3 mb-2'>" +
            "<ul class='list-group list-group-horizontal'>" +
                "<li class='list-group-item'>" + element.name + "</li>" +
                "<li class='list-group-item'>" + element.value + "</li>" +
            "</ul>" +
        "</div>"
        $("#" + name + "AlphanumericDataId").append(data);
    });
    $("#" + name + "AlphanumericDataId").show();
}

function createGraphics(url, form, name) {
    $.ajax({
        url: url,
        type: 'POST',
        data: form,
        processData: false,
        contentType: false,
        success: function(data) {
            newGraphic(name + 'AccelerometerGraphic',['Accelerometer X', 'Accelerometer Y','Accelerometer Z' ] ,[data.accelerometerX, data.accelerometerY, data.accelerometerZ]);
            newGraphic(name + 'GyroscopeGraphic',['Gyroscope X', 'Gyroscope Y','Gyroscope Z'] ,[data.gyroscopeX, data.gyroscopeY, data.gyroscopeZ]);
            newGraphic(name + 'QuaternionGraphic',['Quaternion W', 'Quaternion X', 'Quaternion Y','Quaternion Z'] ,[data.quaternionW, data.quaternionX, data.quaternionY, data.quaternionZ]);
            reload();
            $("#" + name + "Spinner").hide();
            $("#" + name + "AccordionPanelsStayOpen").show();
        },
        async: false
    });
}

function generateMainTest() {
    $('#formUploadFile').submit(function(event) {
        clearAllGraphics();

        $("#mainTable").show();
        $("#spinner").show();
        $("#closeUploadModal").click();
        event.preventDefault();
        var formData = new FormData(this);
        $.ajax({
            url: '/upload-file',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(data) {
                callGenerateMainTest();
                $("#spinner").hide();
                $("#accordionPanelsStayOpenExample").show();

                newGraphic('accelerometerGraphic',['Accelerometer X', 'Accelerometer Y','Accelerometer Z' ] ,[data.accelerometerX, data.accelerometerY, data.accelerometerZ]);
                newGraphic('gyroscopeGraphic',['Gyroscope X', 'Gyroscope Y','Gyroscope Z'] ,[data.gyroscopeX, data.gyroscopeY, data.gyroscopeZ]);
                newGraphic('quaternionGraphic',['Quaternion W', 'Quaternion X', 'Quaternion Y','Quaternion Z'] ,[data.quaternionW, data.quaternionX, data.quaternionY, data.quaternionZ]);

                $('#formUploadFile')[0].reset();
            }
        });
    });
}

function callGenerateMainTest() {
    $.ajax({
        url: '/generate-main-test',
        type: 'GET',
        processData: false,
        contentType: false,
        success: function(data) {
            $("body").append(data);
        },
        async: false
    });
}