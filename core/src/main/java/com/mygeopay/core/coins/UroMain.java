package com.mygeopay.core.coins;

import com.mygeopay.core.coins.families.BitFamily;

import org.bitcoinj.core.Coin;

/**
 * @author FuzzyHobbit
 */
public class UroMain extends CoinType {
    private UroMain() {
        id = "uro.main";

        addressHeader = 68;
        p2shHeader = 5;
        acceptableAddressCodes = new int[] { addressHeader, p2shHeader };
        spendableCoinbaseDepth = 20;

        family = BitFamily.get();
        name = "URO";
        fullname = "Urocoin";
        symbol = "URO";
        uriScheme = "uro";
        bip44Index = 17;
        unitExponent = 8;
        feePerKb = value(100000);
        minNonDust = value(1000); // 0.00001 URO mininput
        softDustLimit = value(100000); // 0.001 URO
        softDustPolicy = SoftDustPolicy.BASE_FEE_FOR_EACH_SOFT_DUST_TXO;
    }

    private static UroMain instance = new UroMain();
    public static synchronized UroMain get() {
        return instance;
    }
}
