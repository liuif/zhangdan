<template>
  <view class="page2">
    <view class="btn" @click="back">返回</view>
    <text class="title">🎉 月度消费行为分析报告 📊</text>

    <view class="report">
      <!-- 报告内容在这里插入 -->
      <text class="content">
        喂～你好呀！👋 欢迎来看看你这个月的精彩花费故事！让我们一起梳理一下，看看你的金钱都去了哪里吧！<br><br>

        1. 本月大事回顾：<br>
        这个月发生了什么大事呢？是不是有什么特别的日子，让你的钱包变得更轻了呢？<br><br>

        2. 本月账本情况：<br>
        你的账本叫做 [ledger_name]，这个月在这里的花费共计 [budget] 元！感觉像是一个小经济学家吗？<br><br>

        3. 本月最常花钱的类型：<br>
        你这个月最常花钱的类型是 [type_name]。嗯，好像你在这个领域颇有心得啊！是不是对它情有独钟呢？<br><br>

        4. 喜欢逛哪里：<br>
        你最常在哪里花钱呢？是不是在 [shopping_mall_name] 或者 [online_store_name]？别害羞，每个人都有自己的购物天堂！<br><br>

        5. 意外之喜：<br>
        这个月有没有一些额外的收入让你喜出望外？是中了大奖还是收到了一份意外的礼物呢？<br><br>

        6. 钱包小窍门：<br>
        在新的一个月里，有没有什么计划？是不是想要省一些钱？别担心，我们有一些建议可以帮你做到！<br><br>

        7. 小财政建议：<br>
        记得做好财政规划哦！也许可以考虑制定一个新的预算，或者学习一些省钱的技巧。别忘了，钱包是个大学问！<br><br>

        8. 亲爱的用户，你真棒！<br>
        不管这个月如何，你都是最棒的！如果需要帮助或者更多小财经建议，随时来找我们吧。记得，每一分钱都值得被关心！
      </text>
    </view>

    <text class="title">🚀 本月消费指导及建议 🌟</text>
    <view class="guidance">
      <!-- 消费指导及建议内容在这里插入 -->
      <text class="content">
        <!-- 🚀 消费指导及建议 🌟<br><br> -->

        1. 本月智能储蓄：<br>
        通过设定本月储蓄目标，我们可以帮助你智能储蓄，确保每月都有一部分收入被储蓄起来。这样即使突发状况发生，你也能有足够的备用金。<br><br>

        2. 本月预算管理：<br>
        制定合理的本月预算计划，有助于你更好地控制每个消费领域的开支。我们提供智能预算工具，帮助你合理分配资金，避免不必要的支出。<br><br>

        3. 本月消费优惠：<br>
        不要错过任何优惠！我们会定期为你推送个性化的优惠信息，确保你在购物时获得最大的实惠。<br><br>

        4. 本月财务规划：<br>
        考虑本月的财务目标，我们可以一起制定财务规划，助你实现更大的理财目标。投资、理财，让你的财富更有增值潜力。<br><br>

        5. 本月购物提醒：<br>
        在购物狂潮中保持理智！我们的购物提醒功能会提醒你当前账户余额，确保你在购物时不会超出预算。<br><br>

        记得，理财从现在开始，我们将陪伴你走向更好的财务未来！💰
      </text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      // 在需要的情况下，可以将报告数据传递到这里
      reportData: {
        user_email: "example@example.com",
        user_name: "用户姓名",
        ledger_name: "账本名称",
        budget: 10000,
        type_name: "购物",
        shopping_mall_name: "购物商场",
      },
    };
  },
  methods: {
    back() {
      uni.navigateBack({
        delta: 1
      });
    },
  },
};
</script>

<style scoped>
.page2 {
  padding: 20px;
}

.title {
  font-size: 20px;
  font-weight: bold;
  color: #2196F3; /* 蓝色 */
  margin-bottom: 20px;
  text-align: center;
}

.report,
.guidance {
  background-color: #f5f5f5;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.content {
  white-space: pre-line; /* 保留换行符 */
}

.btn {
  margin: 10px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  cursor: pointer;
}
</style>
