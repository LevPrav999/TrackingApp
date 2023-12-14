package ru.predprof.trackingapp.utils;

public class Counter {

    public Counter(){

    }

    public int countImt(int height, int weight) {
        return weight / (height * height);
    }



    public int countImtBall(int imt) {
        if (imt < 16) {
            return -2;
        }
        if (imt >= 16 && imt < 18) {
            return -1;
        }
        if (imt >= 18 && imt <= 25) {
            return 2;
        }
        if (imt > 25 && imt <= 30) {
            return -1;
        }
        if (imt > 30) {
            return -2;
        }
        return -2;
    }

    public int countPersonalLevel(int imtBall, int level, int healthStatus) {
        return 5 + imtBall + level + healthStatus;
    }

    public float countPersonalSpeed(int personalLevel) {
        if (personalLevel == 0 || personalLevel == 1) {
            return 7f;
        }
        if (personalLevel >= 2 && personalLevel <= 4) {
            return 10f;
        }
        if (personalLevel >= 5 && personalLevel <= 7) {
            return 15f;
        }
        if (personalLevel >= 8 && personalLevel <= 9) {
            return 20f;
        }
        if (personalLevel == 10) {
            return 25f;
        }
        return 17f;
    }

    public int countTimeOfTravel(int lenOfTravel, int personalSpeed) {
        return lenOfTravel / personalSpeed;
    }

    public int countLevelOfTravelInt(float timeOfTravel) {
        if (timeOfTravel <= 1) {
            return 2;
        }
        if (timeOfTravel <= 2.5) {
            return 5;
        }
        if (timeOfTravel > 2.5) {
            return 8;
        }
        return 5;
    }

    public String countLevelOfTravelStr(int timeOfTravel) {
        if (timeOfTravel <= 1) {
            return "Easy";
        }
        if (timeOfTravel <= 2.5) {
            return "Medium";
        }
        if (timeOfTravel > 2.5) {
            return "Hard";
        }
        return "Medium";
    }

    public int countFinalLevelOfTravelInt(int realLevelOfTravel, int levelOfTravelInt) {
        return (realLevelOfTravel + levelOfTravelInt) / 2;
    }

    public String countFinalLevelOfTravelStr(int realLevelOfTravel, int levelOfTravelInt) {
        if (((realLevelOfTravel + levelOfTravelInt) / 2) <= 3) {
            return "Easy";
        }
        if (((realLevelOfTravel + levelOfTravelInt) / 2) <= 7) {
            return "Medium";
        }
        if (((realLevelOfTravel + levelOfTravelInt) / 2) <= 10) {
            return "Hard";
        }
        return "Medium";
    }

    public int createProposedLevelOfTravel(int personalLevel) {
        return personalLevel;
    }

    public int updateProposedLevelOfTravel(int proposedLevelOfTravel, int levelOfTravelInt, int realLevelOfTravelInt) {
        return proposedLevelOfTravel + ((realLevelOfTravelInt - levelOfTravelInt) / 2);
    }
}
