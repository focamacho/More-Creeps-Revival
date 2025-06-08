package com.morecreepsrevival.morecreeps.common.capabilities;

public interface ILawyerFine {
    int getFine();

    void setFine(int fineIn);

    void addFine(int fineToAdd);

    void takeFine(int fineToTake);
}
