library(tidyr)
library(plyr)
library(dplyr)
library(reshape)
library(ggplot2)

setwd(".")

############################
# Read the dataCollapsed
############################

data = read.csv("./data.csv", stringsAsFactors = TRUE)

data_lg <- data %>% filter(Phone == 'Lg')
data_sam <- data %>% filter(Phone == 'Samsung')

############################
# Plots
############################
par(mar=c(5,6,4,1)+.1)
par(cex.lab=2) # is for y-axis
par(cex.axis=2) # is for x-axis
par(cex.main=2) # is for the main title
boxplot(data_lg$Cpu ~ data_lg$Type, main='LG Cpu Usage', ylab="Cpu Usage (%)", ylim=c(0, max(data_lg$Cpu)))
boxplot(data_sam$Cpu ~ data_sam$Type, main='Samsung Cpu Usage', ylab="Cpu Usage (%)", ylim=c(0, max(data_sam$Cpu)))
boxplot(data_lg$Memory ~ data_lg$Type, main='LG Memory Usage', ylab="Memory Usage (kB)", ylim=c(0, max(data_lg$Memory)))
boxplot(data_sam$Memory ~ data_sam$Type, main='Samsung Memory Usage', ylab="Memory Usage (kB)", ylim=c(0, max(data_sam$Memory)))
boxplot(data_lg$Batterystats ~ data_lg$Type, main='LG Energy Usage', ylab="Energy Usage (J)", ylim=c(0, max(data_lg$Batterystats)))
boxplot(data_sam$Batterystats ~ data_sam$Type, main='Samsung Energy Usage', ylab="Energy Usage (J)", ylim=c(0, max(data_sam$Batterystats)))

######Making the Q-Q plots#####
par(mar=c(5,6,4,1)+.1)
par(cex.lab=1.9) # is for y-axis
par(cex.axis=2) # is for x-axis
par(cex.main=2) # is for the main title
#LG-CPU
#qqnorm(data_lg$Cpu, main = 'Q-Q Plot for Normality (LG-CPU)', ylab = 'LG cpu consumption sample quantile (%)')
#qqline(data_lg$Cpu)
#LG-RELATE-CPU
data_ad_lg <- data_lg %>% filter(Type == 'RELATE')
qqnorm(data_ad_lg$Cpu, main = 'Q-Q Plot for Normality (LG-RELATE)', ylab = 'Cpu Usage Sample Quantile (%)')
qqline(data_ad_lg$Cpu)
#LG-BaseApp-CPU
data_BaseApp_lg <- data_lg %>% filter(Type == 'BaseApp')
qqnorm(data_BaseApp_lg$Cpu, main = 'Q-Q Plot for Normality (LG-BaseApp)', ylab = 'Cpu Usage Sample Quantile (%)')
qqline(data_BaseApp_lg$Cpu)
#LG-Memory
#qqnorm(data_lg$Memory, main = 'Q-Q Plot for Normality (LG-Memory)', ylab = 'LG memory consumption sample quantile (kB)')
#qqline(data_lg$Memory)
par(cex.lab=1.7) # is for y-axis
#LG-RELATE-Memory
qqnorm(data_ad_lg$Memory, main = 'Q-Q Plot for Normality (LG-RELATE)', ylab = 'Memory Usage Sample Quantile (kB)')
qqline(data_ad_lg$Memory)
#LG-BaseApp-Memory
qqnorm(data_BaseApp_lg$Memory, main = 'Q-Q Plot for Normality (LG-BaseApp)', ylab = 'Memory Usage Sample Quantile (kB)')
qqline(data_BaseApp_lg$Memory)
#SAM-CPU
#qqnorm(data_sam$Cpu, main = 'Q-Q Plot for Normality (SAM-CPU)', ylab = 'Samsung cpu consumption sample quantile (%)')
#qqline(data_sam$Cpu)
par(cex.lab=1.9) # is for y-axis
#SAM-RELATE-CPU
data_ad_sam <- data_sam %>% filter(Type == 'RELATE')
qqnorm(data_ad_sam$Cpu, main = 'Q-Q Plot for Normality (SAM-RELATE)', ylab = 'Cpu Usage Sample Quantile (%)')
qqline(data_ad_sam$Cpu)
#SAM-BaseApp-CPU
data_BaseApp_sam <- data_sam %>% filter(Type == 'BaseApp')
qqnorm(data_BaseApp_sam$Cpu, main = 'Q-Q Plot for Normality (SAM-BaseApp)', ylab = 'Cpu Usage Sample Quantile (%)')
qqline(data_BaseApp_sam$Cpu)
#SAM-Memory
#qqnorm(data_sam$Memory, main = 'Q-Q Plot for Normality (SAM-Memory)', ylab = 'Samsung memory consumption sample quantile (kB)')
#qqline(data_sam$Memory)
par(cex.lab=1.7) # is for y-axis
#SAM-RELATE-Memory
qqnorm(data_ad_sam$Memory, main = 'Q-Q Plot for Normality (SAM-RELATE)', ylab = 'Memory Usage Sample Quantile (kB)')
qqline(data_ad_sam$Memory)
#SAM-BaseApp-Memory
qqnorm(data_BaseApp_sam$Memory, main = 'Q-Q Plot for Normality (SAM-BaseApp)', ylab = 'Memory Usage Sample Quantile (kB)')
qqline(data_BaseApp_sam$Memory)
#LG-Energy
#qqnorm(data_lg$Batterystats, main = 'Q-Q Plot for Normality (LG-Energy)', ylab = 'LG energy consumption sample quantile (J)')
#qqline(data_lg$Batterystats)
#LG-RELATE-Energy
qqnorm(data_ad_lg$Batterystats, main = 'Q-Q Plot for Normality (LG-RELATE)', ylab = 'Energy Usage Sample Quantile (J)')
qqline(data_ad_lg$Batterystats)
#LG-BaseApp-Energy
qqnorm(data_BaseApp_lg$Batterystats, main = 'Q-Q Plot for Normality (LG-BaseApp)', ylab = 'Energy Usage Sample Quantile (J)')
qqline(data_BaseApp_lg$Batterystats)
#SAM-Energy
#qqnorm(data_sam$Batterystats, main = 'Q-Q Plot for Normality (SAM-Energy)', ylab = 'Samsung energy consumption sample quantile (J)')
#qqline(data_sam$Batterystats)
#SAM-RELATE-Energy
qqnorm(data_ad_sam$Batterystats, main = 'Q-Q Plot for Normality (SAM-RELATE)', ylab = 'Energy Usage Sample Quantile (J)')
qqline(data_ad_sam$Batterystats)
#SAM-BaseApp-Energy
qqnorm(data_BaseApp_sam$Batterystats, main = 'Q-Q Plot for Normality (SAM-BaseApp)', ylab = 'Energy Usage Sample Quantile (J)')
qqline(data_BaseApp_sam$Batterystats)



#####Shapiro-Wilk tests######
#LG-CPU
#shapiro.test(data_lg$Cpu)
#LG-RELATE-CPU
shapiro.test(data_ad_lg$Cpu)
#LG-BaseApp-CPU
shapiro.test(data_BaseApp_lg$Cpu)
#LG-Memory
#shapiro.test(data_lg$Memory)
#LG-RELATE-Memory
shapiro.test(data_ad_lg$Memory)
#LG-BaseApp-Memory
shapiro.test(data_BaseApp_lg$Memory)
#SAM-CPU
#shapiro.test(data_sam$Cpu)
#SAM-RELATE-CPU
shapiro.test(data_ad_sam$Cpu)
#SAM-BaseApp-CPU
shapiro.test(data_BaseApp_sam$Cpu)
#SAM-Memory
#shapiro.test(data_sam$Memory)
#SAM-RELATE-Memory
shapiro.test(data_ad_sam$Memory)
#SAM-BaseApp-Memory
shapiro.test(data_BaseApp_sam$Memory)
#LG-Energy
#shapiro.test(data_lg$Batterystats)
#LG-RELATE-Energy
shapiro.test(data_ad_lg$Batterystats)
#LG-BaseApp_Energy
shapiro.test(data_BaseApp_lg$Batterystats)
#SAM-Energy
#shapiro.test(data_sam$Batterystats)
#SAM-RELATE-Energy
shapiro.test(data_ad_sam$Batterystats)
#SAM-BaseApp-Energy
shapiro.test(data_BaseApp_sam$Batterystats)
