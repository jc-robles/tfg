package com.imudataprocessor.api.dto.out.test;

import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class DataDTO {

    private List<Float> timestamp = new ArrayList<>();

    private List<Float> accelerometerX = new ArrayList<>();

    private List<Float> accelerometerY = new ArrayList<>();

    private List<Float> accelerometerZ = new ArrayList<>();

    private List<Float> gyroscopeX = new ArrayList<>();

    private List<Float> gyroscopeY = new ArrayList<>();

    private List<Float> gyroscopeZ = new ArrayList<>();

    private List<Float> quaternionW = new ArrayList<>();

    private List<Float> quaternionX = new ArrayList<>();

    private List<Float> quaternionY = new ArrayList<>();

    private List<Float> quaternionZ = new ArrayList<>();

    public void addTimestamp(final Float timestamp) {
        if (CollectionUtils.isEmpty(this.timestamp)) {
            this.timestamp = new ArrayList<>();
        }
        this.timestamp.add(timestamp);
    }

    public void addAccelerometerX(final Float accelerometerX) {
        if (CollectionUtils.isEmpty(this.accelerometerX)) {
            this.accelerometerX = new ArrayList<>();
        }
        this.accelerometerX.add(accelerometerX);
    }

    public void addAccelerometerY(final Float accelerometerY) {
        if (CollectionUtils.isEmpty(this.accelerometerY)) {
            this.accelerometerY = new ArrayList<>();
        }
        this.accelerometerY.add(accelerometerY);
    }

    public void addAccelerometerZ(final Float accelerometerZ) {
        if (CollectionUtils.isEmpty(this.accelerometerZ)) {
            this.accelerometerZ = new ArrayList<>();
        }
        this.accelerometerZ.add(accelerometerZ);
    }

    public void addGyroscopeX(final Float gyroscopeX) {
        if (CollectionUtils.isEmpty(this.gyroscopeX)) {
            this.gyroscopeX = new ArrayList<>();
        }
        this.gyroscopeX.add(gyroscopeX);
    }

    public void addGyroscopeY(final Float gyroscopeY) {
        if (CollectionUtils.isEmpty(this.gyroscopeY)) {
            this.gyroscopeY = new ArrayList<>();
        }
        this.gyroscopeY.add(gyroscopeY);
    }

    public void addGyroscopeZ(final Float gyroscopeZ) {
        if (CollectionUtils.isEmpty(this.gyroscopeZ)) {
            this.gyroscopeZ = new ArrayList<>();
        }
        this.gyroscopeZ.add(gyroscopeZ);
    }

    public void addQuaternionW(final Float quaternionW) {
        if (CollectionUtils.isEmpty(this.quaternionW)) {
            this.quaternionW = new ArrayList<>();
        }
        this.quaternionW.add(quaternionW);
    }

    public void addQuaternionX(final Float quaternionX) {
        if (CollectionUtils.isEmpty(this.quaternionX)) {
            this.quaternionX = new ArrayList<>();
        }
        this.quaternionX.add(quaternionX);
    }

    public void addQuaternionY(final Float quaternionY) {
        if (CollectionUtils.isEmpty(this.quaternionY)) {
            this.quaternionY = new ArrayList<>();
        }
        this.quaternionY.add(quaternionY);
    }

    public void addQuaternionZ(final Float quaternionZ) {
        if (CollectionUtils.isEmpty(this.quaternionZ)) {
            this.quaternionZ = new ArrayList<>();
        }
        this.quaternionZ.add(quaternionZ);
    }
}
