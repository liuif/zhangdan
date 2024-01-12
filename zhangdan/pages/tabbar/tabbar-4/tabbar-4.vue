<template>
	<view>
		<view class="uni-padding-wrap ">
			<uni-segmented-control :current="currentItem" :values="items" :style-type="'text'" :active-color="'#007aff'"
				@clickItem="onClickItem" />
			<!-- <Chart1 /> -->
		</view>
		<view class="content">
			<view class="Content">
				<!-- 选项卡1的内容 -->
				<view v-if="currentItem === 0">
					<picker mode="date" :value="date" fields="year" :start="startDate" :end="endDate"
						@change="bindDateChange">
						<view>{{ date }}</view>
					</picker>
					<view class="container">
						<view class="btns">
							<view class="btn" @click="toPage1">消费趋势图及消费占比图</view>
							<view class="btn" @click="toPage2">用户分析报告及消费指导建议</view>
						</view>
					</view>

					<!-- <Chart1 /> -->
				</view>
				<!-- 选项卡2的内容 -->
				<view v-if="currentItem === 1">
					<picker mode="date" :value="date" fields="month" :start="startDate" :end="endDate"
						@change="bindDateChange">
						<view>{{ date }}</view>
					</picker>
					<view class="btns">
						<view class="btn" @click="toPage3">消费趋势图及消费占比图</view>
						<view class="btn" @click="toPage4">用户分析报告及消费指导建议</view>
					</view>
					<!-- <Chart1 /> -->
				</view>
				<!-- 选项卡3的内容 -->
				<!-- <view v-if="currentItem === 2">
						<text class="content-text">选项卡3的内容</text>
					</view> -->
			</view>
		</view>
	</view>
</template>

<script>
	// import Chart1 from '@pages/tabbar/tabbar-4/charts1.vue';
	import Chart1 from './charts1.vue'; // 或者使用相对路径的其他正确方式

	function getDate(type) {
		const date = new Date();
		let year = date.getFullYear();
		let month = date.getMonth() + 1;
		let day = date.getDate();
		if (type === 'start') {
			year = year - 10;
		} else if (type === 'end') {
			year = year + 10;
		}
		month = month > 9 ? month : '0' + month;
		return `${year}-${month}`;
	}
	export default {
		components: {
			Chart1,
			// 其他的组件...
		},
		data() {
			return {
				items: ['年', '月'],
				currentItem: 0,
				date: new Date().getFullYear().toString(), // 初始值只包含年份
				// date: getDate({
				// 	format: true
				// }),
				startDate: getDate('start'),
				endDate: getDate('end'),
			};
		},
		methods: {
			onClickItem(e) {
				if (this.currentItem !== e.currentIndex) {
					this.currentItem = e.currentIndex;
					if (this.currentItem === 0) {
						// 点击年份选项卡时，默认选择当前年份
						const currentYear = new Date().getFullYear();
						this.date = `${currentYear}`;
					} else if (this.currentItem === 1) {
						// 点击月份选项卡时，默认选择当前年份和当前月份
						const currentDate = new Date();
						const currentYear = currentDate.getFullYear();
						const currentMonth = currentDate.getMonth() + 1;
						const formattedMonth = currentMonth > 9 ? currentMonth : '0' + currentMonth;
						this.date = `${currentYear}-${formattedMonth}`;
					}
				}
			},
			async bindDateChange(e) {
				this.date = e.detail.value.substr(0, 7);
				this.time = this.date;
				// this.page = 1;
				// this.list = [];
				const data = await this.fetchChartData(); // 新增获取数据的方法
				this.$refs.chart1.logstatrt(); // 假设你的消费趋势图组件有一个名为 updateChart 的方法
				// this.index();
			},
			async fetchChartData() {
				// 发送后端请求获取数据
				const response = await axios.post('/api/getTotalIncomeAndExpenseByYear', {
					year: parseInt(this.date),
					email: 'user1@example.com' // 替换为你的用户邮箱
				});
				return response.data;
			},
			toPage1() {
				uni.setStorageSync("date", this.date);
				console.log(this.date);
				uni.navigateTo({
					url: '/pages/tabbar/tabbar-4/charts1'
				})
			},
			toPage2() {
				uni.setStorageSync("date", this.date);
				console.log(this.date);
				uni.navigateTo({
					url: '/pages/tabbar/tabbar-4/page2'
				})
			},
			toPage3() {
				uni.setStorageSync("date", this.date);
				console.log(this.date);
				uni.navigateTo({
					url: '/pages/tabbar/tabbar-4/charts2'
				})
			},
			toPage4() {
				uni.setStorageSync("date", this.date);
				console.log(this.date);
				uni.navigateTo({
					url: '/pages/tabbar/tabbar-4/page4'
				})
			},
		}
	}
</script>

<style>
	.content {
		text-align: center;
		height: 400upx;
		margin-top: 200upx;
	}

	/* .container {
	    display: flex;
	    justify-content: center;
	    align-items: center;
	    height: 100vh;
	} */
	.btns {
		display: flex;
		flex-direction: column;
		align-items: center;

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

	/* 头条小程序组件内不能引入字体 */
	/* #ifdef MP-TOUTIAO */
	@font-face {
		font-family: uniicons;
		font-weight: normal;
		font-style: normal;
		src: url('~@/static/uni.ttf') format('truetype');
	}

	/* #endif */

	/* #ifndef APP-NVUE */
	page {
		display: flex;
		flex-direction: column;
		box-sizing: border-box;
		background-color: #efeff4;
		min-height: 100%;
		height: auto;
	}

	.uni-padding-wrap {
		width: 750rpx;
		padding: 0px 30px;
	}

	.Content {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		justify-content: center;
		align-items: center;
		height: 150px;
		text-align: center;
	}

	.picker-container {
		position: relative;

		top: -100px;
		/* 调整时间选择器的垂直位置 */

	}

	.content-text {
		font-size: 18px;
		color: #333;
	}

	/* #endif */
</style>