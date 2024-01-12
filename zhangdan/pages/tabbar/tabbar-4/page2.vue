<template>
	<view class="page2">
		<view class="btn" @click="back">返回</view>
		<text class="title">🎉 年度消费行为分析报告 📊</text>

		<view class="report">
			<!-- 报告内容在这里插入 -->
			<text class="content">
				哈喽！👋 欢迎来看你过去一年的精彩花费故事！让我们一探究竟，看看你的金钱去哪儿了吧！<br><br>

				1. 大事回顾：<br>
				今年你发生了很多事，我们一起来回顾一下你的账单吧！是不是有什么特别的日子，让你的钱包变得更轻了呢？<br><br>

				2. 账本情况：<br>
				你的账本叫做 {{ reportData.ledger_name.ledger_name }} ，今年你在这里的花费共计 {{reportData.budget}} 元！有没有感觉自己是个小经济学家呢？<br><br>

				3. 消费类型：<br>
				你最常花钱的类型是 {{reportData.type_name}} 。嗯，好像你对这个领域很“专业”嘛！是不是对它情有独钟呢？<br><br>

				4. 喜欢逛哪里：<br>
				你最常在哪里花钱？是不是在 {{reportData.shopping_mall_name}} 或者
				{{reportData.online_store_name}}？别害羞，每个人都有自己的购物天堂！<br><br>

				5. 意外之喜：<br>
				今年有没有一些额外的收入让你喜出望外？也许是中了大奖？抑或是收到了一份意外的礼物？<br><br>

				6. 钱包小窍门：<br>
				在新的一年里，有没有什么计划？是不是想要省一些钱？别担心，我们有一些小窍门可以帮你做到！<br><br>

				7. 小财政建议：<br>
				记得做好财政规划哦！也许可以考虑制定一个新的预算，或者学习一些省钱的技巧。别忘了，钱包是个大学问！<br><br>

				8. 亲爱的用户，你真棒！<br>
				不管你的一年如何，你都是最棒的！如果需要帮助或者更多小财经建议，随时来找我们吧。记得，每一分钱都值得被关心！
			</text>
		</view>

		<text class="title">🚀 消费指导及建议 🌟</text>
		<view class="guidance">
			<!-- 消费指导及建议内容在这里插入 -->
			<text class="content">
				<!-- 🚀 消费指导及建议 🌟<br><br> -->

				1. 智能储蓄：<br>
				通过设定储蓄目标，我们可以帮助你智能储蓄，确保每月都有一部分收入被储蓄起来。这样即使突发状况发生，你也能有足够的备用金。<br><br>

				2. 预算管理：<br>
				制定合理的预算计划，有助于你更好地控制每个消费领域的开支。我们提供智能预算工具，帮助你合理分配资金，避免不必要的支出。<br><br>

				3. 消费优惠：<br>
				不要错过任何优惠！我们会定期为你推送个性化的优惠信息，确保你在购物时获得最大的实惠。<br><br>

				4. 财务规划：<br>
				考虑长期财务目标，我们可以一起制定财务规划，助你实现更大的理财目标。投资、理财，让你的财富更有增值潜力。<br><br>

				5. 购物提醒：<br>
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
		mounted() {
			this.getBookList();
		},
		methods: {
			back() {
				uni.navigateBack({
					delta: 1
				});
			},
			getBookList() {
				uni.request({
					url: '/api/getledger',
					method: 'POST',
					data: {
						email: uni.getStorageSync('email')
					},
					success: (res) => {
						console.log(res);
						if (res.statusCode === 200) {
							this.reportData.ledger_name = res.data.ledger[0];
							this.getCountAll();
						} else {
							uni.showToast({
								title: '获取账本列表失败，请稍后重试',
								icon: 'none'
							})
						}
					},
					fail: () => {
						uni.showToast({
							title: '网络异常，请稍后重试',
							icon: 'none'
						})
					}
				})
			},
			getCountAll() {
				uni.request({
					url: '/api/CountAllbyYear',
					method: 'POST',
					data: {
						ledger_id: uni.getStorageSync('ledger'),
						year:uni.getStorageSync('date'),
					},
					success: (res) => {
						console.log(res);
						if (res.statusCode === 200) {
							this.reportData.budget = res.data.value.toFixed(2);
			
						} else {
							uni.showToast({
								title: '获取账本列表失败，请稍后重试',
								icon: 'none'
							})
						}
					},
					fail: () => {
						uni.showToast({
							title: '网络异常，请稍后重试',
							icon: 'none'
						})
					}
				})
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
		color: #2196F3;
		/* 蓝色 */
		margin-bottom: 20px;
		text-align: center;
	}

	.report {
		background-color: #f5f5f5;
		padding: 20px;
		border-radius: 10px;
		box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
		margin-bottom: 20px;
	}

	.guidance {
		background-color: #E3F2FD;
		/* 浅蓝色 */
		padding: 20px;
		border-radius: 10px;
		box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
	}

	.content {
		white-space: pre-line;
		/* 保留换行符 */
	}

	.container {
		display: flex;
		justify-content: center;
		align-items: center;
		flex-direction: column;
		height: 100vh;
	}

	.btn {
		margin: 10px;
		padding: 10px;
		border: 1px solid #ccc;
		border-radius: 5px;
		cursor: pointer;
	}

	.viewimg {
		height: 100%;
		/* background: #d1e9e9; */
	}
</style>