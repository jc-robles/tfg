package com.imudataprocessor.api.dto.internal;

import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class InternalDataDTO {

    private List<String> timestamp;

    private List<String> accelerometerX;

    private List<String> accelerometerY;

    private List<String> accelerometerZ;

    private List<String> gyroscopeX;

    private List<String> gyroscopeY;

    private List<String> gyroscopeZ;

    private List<String> quaternionW;

    private List<String> quaternionX;

    private List<String> quaternionY;

    private List<String> quaternionZ;

    public void addTimestamp(final String timestamp) {
        if (CollectionUtils.isEmpty(this.timestamp)) {
            this.timestamp = new ArrayList<>();
        }
        this.timestamp.add(timestamp);
    }

    public void addAccelerometerX(final String accelerometerX) {
        if (CollectionUtils.isEmpty(this.accelerometerX)) {
            this.accelerometerX = new ArrayList<>();
        }
        this.accelerometerX.add(accelerometerX);
    }

    public void addAccelerometerY(final String accelerometerY) {
        if (CollectionUtils.isEmpty(this.accelerometerY)) {
            this.accelerometerY = new ArrayList<>();
        }
        this.accelerometerY.add(accelerometerY);
    }

    public void addAccelerometerZ(final String accelerometerZ) {
        if (CollectionUtils.isEmpty(this.accelerometerZ)) {
            this.accelerometerZ = new ArrayList<>();
        }
        this.accelerometerZ.add(accelerometerZ);
    }

    public void addGyroscopeX(final String gyroscopeX) {
        if (CollectionUtils.isEmpty(this.gyroscopeX)) {
            this.gyroscopeX = new ArrayList<>();
        }
        this.gyroscopeX.add(gyroscopeX);
    }

    public void addGyroscopeY(final String gyroscopeY) {
        if (CollectionUtils.isEmpty(this.gyroscopeY)) {
            this.gyroscopeY = new ArrayList<>();
        }
        this.gyroscopeY.add(gyroscopeY);
    }

    public void addGyroscopeZ(final String gyroscopeZ) {
        if (CollectionUtils.isEmpty(this.gyroscopeZ)) {
            this.gyroscopeZ = new ArrayList<>();
        }
        this.gyroscopeZ.add(gyroscopeZ);
    }

    public void addQuaternionW(final String quaternionW) {
        if (CollectionUtils.isEmpty(this.quaternionW)) {
            this.quaternionW = new ArrayList<>();
        }
        this.quaternionW.add(quaternionW);
    }

    public void addQuaternionX(final String quaternionX) {
        if (CollectionUtils.isEmpty(this.quaternionX)) {
            this.quaternionX = new ArrayList<>();
        }
        this.quaternionX.add(quaternionX);
    }

    public void addQuaternionY(final String quaternionY) {
        if (CollectionUtils.isEmpty(this.quaternionY)) {
            this.quaternionY = new ArrayList<>();
        }
        this.quaternionY.add(quaternionY);
    }

    public void addQuaternionZ(final String quaternionZ) {
        if (CollectionUtils.isEmpty(this.quaternionZ)) {
            this.quaternionZ = new ArrayList<>();
        }
        this.quaternionZ.add(quaternionZ);
    }
}
