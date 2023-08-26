import json
import numpy as np
import pandas as pd
import sys
from scipy import signal
from sklearn.decomposition import PCA
from sklearn.linear_model import LinearRegression


def sit2stand(gyrx1, gyry1, gyrz1, Fs):
    # gyrx = np.array(gyrx1)
    gyrx = gyrx1.reshape(len(gyrx1), 1)
    # gyry = np.array(gyry1)
    gyry = []
    gyry = gyry1.reshape(len(gyry1), 1)
    # gyrz = np.array(gyrz1)
    gyrz = []
    gyrz = gyrz1.reshape(len(gyrz1), 1)

    gyr = np.array(gyrx)
    gyr = np.append(gyr, gyry, axis=1)
    gyr = np.append(gyr, gyrz, axis=1)

    pca = PCA(n_components=3, svd_solver='full')
    pca.fit(gyr)
    PCA_gyr = pca.transform(gyr)

    gyr_giro = np.array(PCA_gyr[:, 0])
    gyr_giro = gyr_giro.flatten()

    gyr_giro = signal.savgol_filter(gyr_giro, 25, 2)  # 51
    gyr_giro = signal.medfilt(gyr_giro, 7);  # 13
    gyr_smt = gyr_giro - np.mean(gyr_giro)  # center in 0

    gyr_smt_dw = -np.array(gyr_smt)
    l_gyr = len(gyr_smt)
    minPeakHeight = np.max(gyr_smt) / 2
    peaks_stand_temp, properties = signal.find_peaks(gyr_smt, height=minPeakHeight, distance=int(Fs / 1.5))
    minPeakHeight = np.max(gyr_smt_dw) / 2
    peaks_sit_temp, properties = signal.find_peaks(gyr_smt_dw, height=minPeakHeight, distance=int(Fs / 1.5))

    if peaks_stand_temp.size and peaks_sit_temp.size:
        if peaks_stand_temp[0] < peaks_sit_temp[0]:  # El primer pico debe ser sentandose
            peaks_stand = np.array(peaks_sit_temp)
            peaks_sit = np.array(peaks_stand_temp)
        else:
            peaks_stand = np.array(peaks_stand_temp)
            peaks_sit = np.array(peaks_sit_temp)
        numSit = len(peaks_sit)
        numStand = len(peaks_stand)
        for i in range(len(peaks_stand)):
            if i == 0:
                ti = []
            else:
                ti = np.append(ti, (peaks_stand[i] - peaks_stand[i - 1]))

        tid = np.arange(len(ti)).reshape((-1, 1))
        timp = np.array(ti).reshape((-1, 1))
        model = LinearRegression()
        model.fit(tid, timp)
        model = LinearRegression().fit(tid, timp)
        y_new = model.predict(tid)
        slope = (y_new[len(y_new) - 1] - y_new[0]) / (tid[len(tid) - 1] - tid[0])
        slope = np.around(slope, decimals=1)
        slope = -slope[0]
    else:
        numSit = 0
        slope = 0

    return numSit, slope


# Execution Example
Fs = 100
np.set_printoptions(threshold=np.inf, linewidth=np.nan)
df = np.array(pd.read_csv(sys.argv[1], sep=";"))
gyrx = df[2:, 4].astype(float)
gyry = df[2:, 5].astype(float)
gyrz = df[2:, 6].astype(float)

numSteps, gait_sym = sit2stand(gyrx, gyry, gyrz, Fs)
result = {
    "Number of sits": str(numSteps),
    "Slope": str(gait_sym)
}
print(json.dumps(result, indent=4))
