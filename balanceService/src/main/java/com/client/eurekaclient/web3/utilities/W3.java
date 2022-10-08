package com.client.eurekaclient.web3.utilities;//package com.client.eurekaclient.web3.utilities;
//
//import com.client.eurekaclient.web3.contracts.nft.OreChainNFT;
//import org.web3j.contracts.eip20.generated.ERC20;
//import org.web3j.contracts.eip721.generated.ERC721;
//import org.web3j.crypto.Credentials;
//import org.web3j.protocol.Web3j;
//import org.web3j.protocol.http.HttpService;
//import org.web3j.tx.gas.DefaultGasProvider;
//
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.math.RoundingMode;
//
//
//public class W3 {
//    public static Web3j web3j = Web3j.build(new HttpService(Constants.blockChainUrl));
//    private static Credentials credentials = Credentials.create(Constants.privateKey);
//    private static ERC20 erc20Token = ERC20.load(Constants.ERC20ContractAddress, web3j, credentials, new DefaultGasProvider());
//
//    public static BigDecimal getBalance(String ownerAddress) throws Exception {
//        return new BigDecimal(erc20Token.balanceOf(ownerAddress).send()).divide(new BigDecimal(1000000000000000000L), 18, RoundingMode.HALF_UP);
//    }
//
//    public static void approveTo(String spender, BigInteger approvingValue) throws Exception {
//        erc20Token.approve(spender, approvingValue).send();
//    }
//
//    public static void transferTo(String to, BigInteger value) throws Exception {
//        erc20Token.transfer(to, value.multiply(BigInteger.valueOf(1_000_000_000_000_000_000L))).send();
//    }
//
//    public static OreChainNFT getContract() {
//        return OreChainNFT.load(Constants.NFTContractAddress,
//                Web3j.build(new HttpService(Constants.blockChainUrl)),
//                Credentials.create(Constants.privateKey),
//                new DefaultGasProvider().getGasPrice(),
//                new DefaultGasProvider().getGasLimit());
//    }
//
//    public static OreChainNFT getTestContract() {
//        return OreChainNFT.load(Constants.NFTtestContractAddress,
//                Web3j.build(new HttpService(Constants.blockChainUrl)),
//                Credentials.create(Constants.privateKey),
//                new DefaultGasProvider().getGasPrice(),
//                new DefaultGasProvider().getGasLimit());
//    }
//
//    public static ERC20 getERC20Contract() {
//        return ERC20.load(Constants.ERC20ContractAddress, web3j, credentials, new DefaultGasProvider());
//    }
//
//    public static ERC721 getERC721Contract() {
//        return ERC721.load(Constants.NFTContractAddress, web3j, credentials, new DefaultGasProvider());
//    }
//
////    public static TestNFT getTestNFTtoken() {
////        return TestNFT.load(Constants.NFTtestContractAddress,
////                Web3j.build(new HttpService(Constants.blockChainUrl)),
////                Credentials.create(Constants.privateKey),
////                new DefaultGasProvider().getGasPrice(),
////                new DefaultGasProvider().getGasLimit());
////    }
//}
