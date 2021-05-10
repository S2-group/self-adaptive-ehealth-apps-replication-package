# self-adaptive-ehealth-apps-replication-package
Replication package of the paper titled "An Evaluation of the Effectiveness of Personalizationand Self-Adaptation for e-Health Apps".

This study has been designed, developed, and reported by the following investigators:

- [Eoin Martino Grua](https://emgrua.github.io/) (Vrije Universiteit Amsterdam, University of Limerick)
- [Martina De Sanctis](https://martinadesanctis.bitbucket.io/index.html) (Gran Sasso Science Institute) 
- [Ivano Malavolta](https://www.ivanomalavolta.com) (Vrije Universiteit Amsterdam)
- [Mark Hoogendoorn](https://www.cs.vu.nl/~mhoogen/) (Vrije Universiteit Amsterdam)
- [Patricia Lago](https://www.cs.vu.nl/~patricia/Patricia_Lago/Home.html) (Vrije Universiteit Amsterdam, Chalmers University of Technology)

For any information, interested researchers can contact us by sending an email to any of the investigators listed above.
The full dataset including raw data, mining scripts, and analysis scripts produced during the study are available below.

## How to cite the dataset
If the dataset is helping your research, consider to cite it is as follows, thanks!

```
TBD
```

### Overview of the replication package
---

This replication package is structured as follows:

```
    /
    .
    |--- MeasurementBased-DataAnalysis/       		The data that has been extracted from the measurement based experiments and the R script for plotting the extracted data (see below).
    |--- MeasurementBasedExperiment-Data/         The full dataset from the measurement based experiments as collected and returned by Android Runner.
    |--- UserStudy-Data/   	                      The full dataset from the user studies carried out. All of the data has been anonymized.
    |--- RAforEHealth-Implementation/          All of the code for both version of the mobile app used in our experiments (BaseApp and RELATE) as well as the code for implementing the Back-end          
    |--- RELATE_App_Flow                          Appendix to the main research paper containing the App flow for RELATE
```
Each of the folders listed above are described in details in the remaining of this readme.

### Data Analysis
---
```
MeasurementBased-DataAnalysis
    .
    |--- data.csv                                 The ornanized version of the collected data used by the analysis.r script
    |--- analysis.r                               The R script we used for generating the plots reported in the article
```
### Dataset
---
```
MeasurementBasedExperiment-Data
  .
  |--- Lg-BaseApp.zip                              Zip folder containing all of the measurement based data collected for the Lg smartphone using the BaseApp
  |--- Lg-RELATE.zip                               Zip folder containing all of the measurement based data collected for the Lg smartphone using RELATE
  |--- Samsung-BaseApp.zip                         Zip folder containing all of the measurement based data collected for the Samsung smartphone using the BaseApp
  |--- Samsung-RELATE.zip                          Zip folder containing all of the measurement based data collected for the Samsung smartphone using RELATE
```
```

UserStudy-Data
  .
  |--- Anonymized Daily Survey Data (Responses).xlsx  All of daily survey replies collected from the participants of the user study
  |--- Anonymized End Survey Data (Responses).xlsx    All of the end survey replies collected from the partipants of the user study
  |--- Anonymized Initial Survey Data (Responses).xlsx All of the initial survey replies collected from the participants that were selected to partake in the user study
```
### Implementation
---
```
RAforEHealth-Implementation
.
|--- Backend/
    |--- demofile2.txt Plain text file used to store the unique tokens received
    |--- FireBaseMessaging.py Python file containing the functions used to send UserProcesses to the participants
    |--- receive_token.py Python file containing the functions used to receive the unique tokens from the deployed apps
    |--- __init__.py Empty init python file
|--- eHealthAppBase_(BaseApp)/ The Android project used to create the BaseApp as exported from Android Studio
|--- eHealthApp_(RELATE)/ The Android project used to create RELATE as exported from Android Studio

## License

This software is licensed under the MIT License.
