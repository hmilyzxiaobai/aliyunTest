package example.dongfangcaifu.service;

public class NoteServiceHis {
}



//
//        for(FinancialInfoDmEntity financialInfoDmEntity:savesCodeDfList){
//
//
//            index++;
//
//            if (financialInfoDmEntity.getCompanyCode().startsWith("688")
//                    || financialInfoDmEntity.getCompanyCode().startsWith("668")){
//                continue;
//            }
//            System.out.println(index);
//
//
//            int comHis = companyHistoryService.judgeAug(financialInfoDmEntity.getCompanyCode(), financialInfoDmEntity.getNowPrice());
//            // 0 1 2
//            int capHis = capitalFlowHistoryService.judgeUpMul(
//                    financialInfoDmEntity.getCompanyName(),
//                    financialInfoDmEntity.getCompanyCode());
//            // 0 3 4
//            companyOtherService.judgeLow(dataExcel,financialInfoDmEntity);
//            if ((capHis*comHis)>0){
//
//                if (!financialInfoDmEntity.getCompanyCode().startsWith("688")){
//                    mapScore.put(financialInfoDmEntity.getCompanyCode(),capHis*comHis);
//
//                    holdOnService.saveOne(financialInfoDmEntity.getCompanyName(),
//                            financialInfoDmEntity.getCompanyCode()
//                            ,financialInfoDmEntity.getNowPrice(),"100");
//
//                }
//            }
//
//            String nowPrice = financialInfoDmEntity.getNowPrice();
//
//
//            Float nowPriceFloat = FloatUtils.stringToFloat(nowPrice);
//
//            if (nowPriceFloat!=0){
//                String pressureStr = pressureMap.get(financialInfoDmEntity.getCompanyCode());
//                String supportStr = supporeMap.get(financialInfoDmEntity.getCompanyCode());
//
//                // 总的涨幅情况
//                float ins = culSupPre(supportStr, pressureStr);
//
//                if (ins==0){
//                    continue;
//                }
//
//                Float support = FloatUtils.stringToFloat(supportStr);
//                Float pressure = FloatUtils.stringToFloat(pressureStr);
//                if (((nowPriceFloat-support)*100/support) > ins*0.1
//                        && ((nowPriceFloat-support)*100/support) < ins*0.3){
//                    // 在百分之20到30之间  就可以买入
//                    messageSupPre.put(financialInfoDmEntity.getCompanyCode(),"当前股票为"+financialInfoDmEntity.getCompanyName()+"("+financialInfoDmEntity.getCompanyCode()+")"+",当前股值为："+nowPriceFloat+"在支撑位的涨幅为"+
//                            String.format("%.2f",((nowPriceFloat-support)*100/support))+",该股票支撑位为"+support+",压力位为："+pressure+"，总的涨幅幅度为"+ins);
//
//                }
//
//                if (nowPriceFloat>pressure){
//                    messageSupPre.put(financialInfoDmEntity.getCompanyCode(),"当前股值为："+nowPriceFloat+"超过了压力位，或许会继续涨,超过压力位百分比："+
//                            String.format("%.2f",((nowPriceFloat-pressure)*100/pressure)));
//
//                }
//            }
//
//            // 汇总
////
////            JudgeVo judge = companyHistoryService.judge(financialInfoDmEntity.getProportion(),financialInfoDmEntity.getCompanyCode(),
////                    financialInfoDmEntity.getEarnings(), financialInfoDmEntity.getNowPrice());
////            // 第一步判断
////            // 第二步判断
////            CapitalFlowHistoryResponse response = capitalFlowHistoryService.judgeByCapitalHis(financialInfoDmEntity);
////
////            if (judge.getJudgeFlag() && response.getBuy()){
////                // 推送邮箱
////                String sendMessage = judge.getMessage()+"\n\n"+response.getMessage();
////
////                log.info(financialInfoDmEntity.getCompanyCode()
////                        +financialInfoDmEntity.getCompanyName()+"推送邮箱消息为："+sendMessage);
////                try {
////                    if (sendHisService.judge(financialInfoDmEntity.getCompanyName(),financialInfoDmEntity.getCompanyCode(),sendMessage,format,0)){
////                        if (!financialInfoDmEntity.getCompanyCode().startsWith("688")){
////                            emailService.sendEmail("2953872785@qq.com,3620165237@qq.com",financialInfoDmEntity.getCompanyName()+"--"+financialInfoDmEntity.getCompanyCode(),sendMessage);
////                        }
////                    }
////
////                    // 模拟  尽快买入
////                }catch (Exception e){
////                    e.printStackTrace();
////                }
////            }
//
//
//            /**
//             *  判断是否为钩子买入
//             */
////            GouResponse gouResponse = capitalFlowHistoryService.gouCul(financialInfoDmEntity);
////            if (gouResponse.isGou()){
////
////                StringBuilder sb = new StringBuilder();
////                Map<String, String> gouHis = gouResponse.getGouHis();
////                for(String key:gouHis.keySet()){
////                    sb.append(key).append(",").append(gouHis.get(key)).append("\n");
////                }
////
////                log.info(financialInfoDmEntity.getCompanyCode()+financialInfoDmEntity.getCompanyName()
////                        +"推送钩子信息："+sb.toString());
////                try {
////                    if (sendHisService.judge(financialInfoDmEntity.getCompanyName(),financialInfoDmEntity.getCompanyCode(), sb.toString(), format,1)) {
////                        if (!financialInfoDmEntity.getCompanyCode().startsWith("688")) {
////                           emailService.sendEmail("2953872785@qq.com,3620165237@qq.com", "钩子股票" + financialInfoDmEntity.getCompanyName() + "--" + financialInfoDmEntity.getCompanyCode(), sb.toString());
////                        }
////                    }
////                    // 模拟  尽快买入
////                }catch (Exception e){
////                    e.printStackTrace();
////                }
////            }
//        }
//        if (!CollectionUtils.isEmpty(mapScore)){
//            for(String code:mapScore.keySet()){
//                System.out.println("股票代码为："+code+",得分为："+mapScore.get(code));
//            }
//        }
//
//        if (!CollectionUtils.isEmpty(messageSupPre)){
//            for(String code:messageSupPre.keySet()){
//                System.out.println("股票代码为："+code+","+messageSupPre.get(code));
//            }
//        }
//        if (!CollectionUtils.isEmpty(mapScore)){
//            for(String code:mapScore.keySet()){
//                System.out.println("股票代码为："+code+",得分为："+mapScore.get(code));
//            }
//        }
//
//        if (!CollectionUtils.isEmpty(messageSupPre)){
//            for(String code:messageSupPre.keySet()){
//                System.out.println("股票代码为："+code+",得分为："+messageSupPre.get(code));
//            }
//        }

//            index++;
//
//            // 判断 是不是最低点
//            companyOtherService.judgeLow(dataExcel,financialInfoDmEntity);
//
//
//            // 持续增长
//            if (FloatUtils.stringToFloat(financialInfoDmEntity.getEarnings())>0&&
//                    capitalFlowHistoryService.judgeUpSureUpSure(financialInfoDmEntity)){
//                System.out.println("值得买入"+financialInfoDmEntity.getCompanyCode());
//            }
//            if (financialInfoDmEntity.getCompanyCode().startsWith("688")
//            || financialInfoDmEntity.getCompanyCode().startsWith("668")){
//                continue;
//            }
//            System.out.println(index);
//
////
//            int comHis = companyHistoryService.judgeAug(financialInfoDmEntity.getCompanyCode(), financialInfoDmEntity.getNowPrice());
////            // 0 1 2
//            int capHis = capitalFlowHistoryService.judgeUpMul(
//                    financialInfoDmEntity.getCompanyName(),
//                    financialInfoDmEntity.getCompanyCode());
//            // 0 3 4
//            if ((capHis*comHis)>0){
//
//                if (!financialInfoDmEntity.getCompanyCode().startsWith("688")){
//                    mapScore.put(financialInfoDmEntity.getCompanyCode(),capHis*comHis);
//
//                    holdOnService.saveOne(financialInfoDmEntity.getCompanyName(),
//                            financialInfoDmEntity.getCompanyCode()
//                    ,financialInfoDmEntity.getNowPrice(),"100");
//
//                }
//            }

//            String nowPrice = financialInfoDmEntity.getNowPrice();
////
////
//            Float nowPriceFloat = FloatUtils.stringToFloat(nowPrice);
////
//            if (nowPriceFloat!=0){
//                String pressureStr = pressureMap.get(financialInfoDmEntity.getCompanyCode());
//                String supportStr = supporeMap.get(financialInfoDmEntity.getCompanyCode());
//
//                // 总的涨幅情况
//                float ins = culSupPre(supportStr, pressureStr);
//
//                if (ins==0){
//                    continue;
//                }
//                if (ins<=20){
//                    continue;
//                }
//
//                Float support = FloatUtils.stringToFloat(supportStr);
//                Float pressure = FloatUtils.stringToFloat(pressureStr);
//                if (((nowPriceFloat-support)*100/support) > ins*0.1
//                && ((nowPriceFloat-support)*100/support) < ins*0.3){
//                    // 在百分之20到30之间  就可以买入
//                    messageSupPre.put(financialInfoDmEntity.getCompanyCode(),"当前股票为"+financialInfoDmEntity.getCompanyName()+"("+financialInfoDmEntity.getCompanyCode()+")"+",当前股值为："+nowPriceFloat+"在支撑位的涨幅为"+
//                            String.format("%.2f",((nowPriceFloat-support)*100/support))+",该股票支撑位为"+support+",压力位为："+pressure+"，总的涨幅幅度为"+ins);
//
//                }
//
//                if (nowPriceFloat>pressure){
//                    messageSupPre.put(financialInfoDmEntity.getCompanyCode(),"当前股值为："+nowPriceFloat+"超过了压力位，或许会继续涨,超过压力位百分比："+
//                            String.format("%.2f",((nowPriceFloat-pressure)*100/pressure)));
//
//                }
//            }


//            capitalFlowHistoryService.judgeUpUpUp(financialInfoDmEntity);
//
//           //  汇总
////
//            JudgeVo judge = companyHistoryService.judge(financialInfoDmEntity.getProportion(),financialInfoDmEntity.getCompanyCode(),
//                    financialInfoDmEntity.getEarnings(), financialInfoDmEntity.getNowPrice());
//            // 第一步判断
//            // 第二步判断
//            CapitalFlowHistoryResponse response = capitalFlowHistoryService.judgeByCapitalHis(financialInfoDmEntity);
//
//            if (judge.getJudgeFlag() && response.getBuy()){
//                // 推送邮箱
////                String sendMessage = judge.getMessage()+"\n\n"+response.getMessage();
////
////                log.info(financialInfoDmEntity.getCompanyCode()
////                        +financialInfoDmEntity.getCompanyName()+"推送邮箱消息为："+sendMessage);
////                try {
////                    if (sendHisService.judge(financialInfoDmEntity.getCompanyName(),financialInfoDmEntity.getCompanyCode(),sendMessage,format,0)){
////                        if (!financialInfoDmEntity.getCompanyCode().startsWith("688")){
////                            emailService.sendEmail("2953872785@qq.com,3620165237@qq.com",financialInfoDmEntity.getCompanyName()+"--"+financialInfoDmEntity.getCompanyCode(),sendMessage);
////                        }
////                    }
////
////                    // 模拟  尽快买入
////                }catch (Exception e){
////                    e.printStackTrace();
////                }
//            }


/**
 *  判断是否为钩子买入
 */
//            GouResponse gouResponse = capitalFlowHistoryService.gouCul(financialInfoDmEntity);
//            if (gouResponse.isGou()){
//
//                StringBuilder sb = new StringBuilder();
//                Map<String, String> gouHis = gouResponse.getGouHis();
//                for(String key:gouHis.keySet()){
//                    sb.append(key).append(",").append(gouHis.get(key)).append("\n");
//                }
//
//                log.info(financialInfoDmEntity.getCompanyCode()+financialInfoDmEntity.getCompanyName()
//                        +"推送钩子信息："+sb.toString());
//                try {
//                    if (sendHisService.judge(financialInfoDmEntity.getCompanyName(),financialInfoDmEntity.getCompanyCode(), sb.toString(), format,1)) {
//                        if (!financialInfoDmEntity.getCompanyCode().startsWith("688")) {
//                           emailService.sendEmail("2953872785@qq.com,3620165237@qq.com", "钩子股票" + financialInfoDmEntity.getCompanyName() + "--" + financialInfoDmEntity.getCompanyCode(), sb.toString());
//                        }
//                    }
//                    // 模拟  尽快买入
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }