<template>
	<view class="viewimg">
		<view>
			<view class="btn" @click="back">返回</view>
			<!-- <view>消费趋势图</view> -->
			<view
				style="display: flex; justify-content: center; align-items: center; height: 4vh; font-size: 20px; font-weight: bold;">
				消费趋势图
			</view>
			<echarts :option="option" style="height: 300px;"></echarts>
			<!-- <button @click="updateClick">更新数据</button> -->
		</view>
		<view
			style="display: flex; justify-content: center; align-items: center; height: 4vh; font-size: 20px; font-weight: bold;">
			消费类型占比图
		</view>
		<view>
			<view style="display: flex; justify-content: center; align-items: center; height: 4vh; font-size: 20px; font-weight: bold;">支出：</view>
			<echarts :option="optionone" style="height: 400px;"></echarts>
		</view>
		<view>
			<view style="display: flex; justify-content: center; align-items: center; height: 4vh; font-size: 20px; font-weight: bold;">收入：</view>
			<echarts :option="optiontwo" style="height: 400px;"></echarts>
		</view>
	</view>
</template>

<script>
	import echarts from '@/components/echarts/echarts.vue';
	export default {
		data() {
			return {
				option: {},
				optionone: {},
				optiontwo: {}
			};
		},
		onLoad() {
			console.log(777777);
		},
		components: {
			echarts
		},
		mounted() {
			this.logstatrt();
			this.logstatrtone();
			this.logstatrttwo();
		},
		methods: {
			back() {
				uni.navigateBack({
					delta: 1
				})
			},
			logstatrt() {
				console.log('初次调用');
				this.option = {
					backgroundColor: '#011246',
					tooltip: {
						trigger: 'axis',
						axisPointer: {
							type: 'shadow'
						}
					},
					grid: {
						top: '25%',
						right: '45',
						bottom: '20',
						left: '30'
					},
					legend: {
						data: [{
							name: '收入'
						}, {
							name: '支出'
						}, {
							name: '结余'
						}, {
							name: '收入增速'
						}, {
							name: '支出增速'
						}, {
							name: '结余增速'
						}],
						top: '0%',
						textStyle: {
							color: 'rgba(255,255,255,0.9)' //图例文字
						}
					},
					dataZoom: [{
						type: 'slider',
						realtime: true,
						//移动端展示方式
						handleSize: '100%', //滑动条两侧的大小
						start: 0,
						end: 50
					}],
					grid: {
						left: '3%',
						right: '4%',
						bottom: '15%',
						height: '70%',
						containLabel: true
					},
					xAxis: [{
						type: 'category',
						data: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15',
							'16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28',
							'29', '30', '31'
						],
						axisLine: {
							lineStyle: {
								color: 'rgba(255,255,255,.1)'
							}
						},
						axisLabel: {
							textStyle: {
								color: 'rgba(255,255,255,.7)',
								fontSize: '14'
							}
						}
					}],
					yAxis: [{
							type: 'value',
							name: '元',
							splitLine: {
								show: false
							},
							axisLabel: {
								show: true,
								fontSize: 14,
								color: 'rgba(255,255,255,.6)'
							},
							axisLine: {
								min: 0,
								max: 10,
								lineStyle: {
									color: 'rgba(255,255,255,.4)'
								}
							} //左线色
						},
						// {
						// 	type: 'value',
						// 	name: '增速',
						// 	show: true,
						// 	axisLabel: {
						// 		show: true,
						// 		fontSize: 14,
						// 		formatter: '{value} %',
						// 		color: 'rgba(255,255,255,.6)'
						// 	},
						// 	axisLine: { lineStyle: { color: 'rgba(255,255,255,.4)' } }, //右线色
						// 	splitLine: { show: true, lineStyle: { color: 'rgba(255,255,255,.1)' } } //x轴线
						// }
					],
					series: [
						// 				{
						// 					name: '收入',
						// 					type: 'bar',
						// 					data: [35, 36.6, 38.8, 40.84, 41.6],
						// 					// "barWidth": "30",
						// 					itemStyle: {
						// 						normal: {
						// 							color: {
						// 								type: 'linear',
						// 								x: 0.5,
						// 								y: 0.5,
						// 								r: 0.5,
						// 								colorStops: [
						// 									{
						// 										offset: 0,
						// 										color: '#00FFE3' // 0% 处的颜色
						// 									},
						// 									{
						// 										offset: 1,
						// 										color: '#4693EC' // 100% 处的颜色
						// 									}
						// 								],
						// 								globalCoord: false // 缺省为 false
						// 							}
						// 						}
						// 					}
						// 					// "barGap": "0.2"
						// 				},
						// 				{
						// 					name: '支出',
						// 					type: 'bar',
						// 					data: [16, 14.8, 14.1, 15, 16.3],

						// 					color: {
						// 						type: 'linear',
						// 						x: 0.5,
						// 						y: 0.5,
						// 						r: 0.5,
						// 						colorStops: [
						// 							{
						// 								offset: 0,
						// 								color: '#248ff7' // 0% 处的颜色
						// 							},
						// 							{
						// 								offset: 1,
						// 								color: '#6851f1' // 100% 处的颜色
						// 							}
						// 						],
						// 						globalCoord: false // 缺省为 false
						// 					}
						// 				},
						// 				{
						// 					name: '结余',
						// 					type: 'bar',
						// 					data: [19, 27.4, 24.7, 25.84, 25.3],
						// 					color: {
						// 						type: 'linear',
						// 						x: 0.5,
						// 						y: 0.5,
						// 						r: 0.5,
						// 						colorStops: [
						// 							{
						// 								offset: 0,
						// 								color: '#fccb05' // 0% 处的颜色
						// 							},
						// 							{
						// 								offset: 1,
						// 								color: '#f5804d' // 100% 处的颜色
						// 							}
						// 						],
						// 						globalCoord: false // 缺省为 false
						// 					}
						// 				},
						{
							name: '收入',
							type: 'line',
							yAxisIndex: 0,

							data: [0, 6.01, 5.26, 1.48, 25, 37, 66, 34, 66, 0, 0, 0, 0, 0, 0, 0, 100, 2, 2, 2, 56,
								7
							],
							lineStyle: {
								normal: {
									width: 2
								}
							},
							itemStyle: {
								normal: {
									color: '#86d370'
								}
							},
							smooth: true
						},
						{
							name: '支出',
							type: 'line',
							yAxisIndex: 0,

							data: [0, -4.73, 6.38, 8.67],
							lineStyle: {
								normal: {
									width: 2
								}
							},
							itemStyle: {
								normal: {
									color: '#3496f8'
								}
							},
							smooth: true
						},
						{
							name: '结余',
							type: 'line',
							yAxisIndex: 0,

							data: [0, -1.09, 8.24, -9.64],
							lineStyle: {
								normal: {
									width: 2
								}
							},
							itemStyle: {
								normal: {
									color: '#fbc30d'
								}
							},
							smooth: true
						}
					]
				};
				const data1 = {
					date: uni.getStorageSync("date"),
					email: uni.getStorageSync('email')
				};
				console.log(data1);
				uni.request({
					url: '/api/getTotalIncomeAndExpenseByMonth',
					method: 'POST',
					data: {
						date: uni.getStorageSync("date"),
						email: uni.getStorageSync('email')
					},
				}).then((res) => {
					const result = res[1].data;
					if (!result) {
						console.error('登录接口返回数据为空');
						return;
					}
					if (result.code === 1) {
						console.log(result.inandout);
						// 登录成功
						const inArray = Array(31).fill(0);
						const outArray = Array(31).fill(0);
						const balArray = Array(31).fill(0);

						// 遍历原始数据并填充数组
						result.inandout.forEach(item => {
							const day = item.day;
							const inValue = item.in;
							const outValue = item.out;
							const balance = item.in - item.out;

							inArray[day - 1] = inValue;
							outArray[day - 1] = outValue;
							balArray[day - 1] = balance;
						});

						console.log(inArray); // in 数组
						console.log(outArray); // out 数组

						this.option.series[0].data = outArray;
						this.option.series[1].data = inArray;
						this.option.series[2].data = balArray;
						this.logstatrtone();


					} else {
						// 登录失败，显示错误消息
						uni.showToast({
							title: result.msg,
							icon: 'none'
						});
					}
				}).catch((error) => {
					console.log('请求失败', error);
				});
			},
			logstatrtone() {
				this.optionone = {
					backgroundColor: '#0c1e55',

					color: ['#00a0fc', '#ffe400', '#ff9a09', '#ef1e5f', '#23cbe5', '#ec561b', '#ffa500', '#dddf00',
						'#b23aee', '#50b332'
					],

					tooltip: {
						trigger: 'item',
						// formatter: "   {a} <br/>{b} : {c} ({d}%)"
						formatter: "   支出：{c}"
					},
					legend: { // 图例-图上面的分类
						// orient: 'vertical',
						// right: 30,
						//   icon: 'rect',//长方形
						icon: 'circle',
						top: "1%",
						itemWidth: 10,
						itemHeight: 10,
						// itemGap: 13,
						data: ['交通', '日用', '住房', '餐饮', '办公', '娱乐', '购物'],
						// right: '56%',
						textStyle: {
							fontSize: 14,
							color: '#a6cde8',
							lineHeight: 49
						},
						formatter: function(name) {
							return "" + name + ""
						},
						padding: [2, 2]
					},
					grid: {
						top: '20%',
						left: '53%',
						right: '10%',
						bottom: '6%',
						containLabel: true
					},

					series: [{
						label: {
							normal: {
								formatter: '{b|{b}：} {c} \n  {per|{d}%} ',
								rich: {}
							},
							emphasis: {
								show: true,
							}
						},
						// labelLine: {
						//     normal: {
						//         show: false
						//     }
						// },
						name: '访问来源',
						type: 'pie',
						radius: '55%',
						center: ['50%', '60%'],
						data: [{
								value: 195,
								name: '交通'
							},
							{
								value: 185,
								name: '日用'
							},
							{
								value: 260,
								name: '住房'
							},
							{
								value: 213,
								name: '餐饮'
							},
							{
								value: 52,
								name: '办公'
							},
							{
								value: 35,
								name: '娱乐'
							},
							{
								value: 46,
								name: '购物'
							},
						],
						itemStyle: {
							emphasis: {
								shadowBlur: 10,
								shadowOffsetX: 0,
								shadowColor: 'rgba(0, 0, 0, 0.5)'
							}
						},
						labelLine: {
							normal: {
								length: 5,
								length2: 1,
								smooth: true,
							}
						},
					}]
				};
				uni.request({
					url: '/api/findDailyIncomeExpenseNetByMonth',
					method: 'POST',
					data: {
						date: uni.getStorageSync("date"),
						email: uni.getStorageSync('email')
					},
				}).then((res) => {
					const result = res[1].data;
					if (!result) {
						console.error('登录接口返回数据为空');
						return;
					}
					if (result.code === 1) {
						console.log(result.in);
						console.log(result.out);
						// 登录成功
						this.optionone.series[0].data = result.out;

					} else {
						// 登录失败，显示错误消息
						uni.showToast({
							title: result.msg,
							icon: 'none'
						});
					}
				}).catch((error) => {
					console.log('请求失败', error);
				});
			},
			logstatrttwo() {
				this.optiontwo = {
					backgroundColor: '#0c1e55',

					color: ['#00a0fc', '#ffe400', '#ff9a09', '#ef1e5f', '#23cbe5', '#ec561b', '#ffa500', '#dddf00',
						'#b23aee', '#50b332'
					],

					tooltip: {
						trigger: 'item',
						// formatter: "   {a} <br/>{b} : {c} ({d}%)"
						formatter: "   收入：{c}"
					},
					legend: { // 图例-图上面的分类
						// orient: 'vertical',
						// right: 30,
						//   icon: 'rect',//长方形
						icon: 'circle',
						top: "1%",
						itemWidth: 10,
						itemHeight: 10,
						// itemGap: 13,
						data: ['交通', '日用', '住房', '餐饮', '办公', '娱乐', '购物'],
						// right: '56%',
						textStyle: {
							fontSize: 14,
							color: '#a6cde8',
							lineHeight: 49
						},
						formatter: function(name) {
							return "" + name + ""
						},
						padding: [2, 2]
					},
					grid: {
						top: '20%',
						left: '53%',
						right: '10%',
						bottom: '6%',
						containLabel: true
					},

					series: [{
						label: {
							normal: {
								formatter: '{b|{b}：} {c} \n  {per|{d}%} ',
								rich: {}
							},
							emphasis: {
								show: true,
							}
						},
						// labelLine: {
						//     normal: {
						//         show: false
						//     }
						// },
						name: '访问来源',
						type: 'pie',
						radius: '55%',
						center: ['50%', '60%'],
						data: [{
								value: 195,
								name: '交通'
							},
							{
								value: 185,
								name: '日用'
							},
							{
								value: 260,
								name: '住房'
							},
							{
								value: 213,
								name: '餐饮'
							},
							{
								value: 52,
								name: '办公'
							},
							{
								value: 35,
								name: '娱乐'
							},
							{
								value: 46,
								name: '购物'
							},
						],
						itemStyle: {
							emphasis: {
								shadowBlur: 10,
								shadowOffsetX: 0,
								shadowColor: 'rgba(0, 0, 0, 0.5)'
							}
						},
						labelLine: {
							normal: {
								length: 5,
								length2: 1,
								smooth: true,
							}
						},
					}]
				};
				uni.request({
					url: '/api/findDailyIncomeExpenseNetByMonth',
					method: 'POST',
					data: {
						date: uni.getStorageSync("date"),
						email: uni.getStorageSync('email')
					},
				}).then((res) => {
					const result = res[1].data;
					if (!result) {
						console.error('登录接口返回数据为空');
						return;
					}
					if (result.code === 1) {
						console.log(result.in);
						console.log(result.out);
						// 登录成功
						this.optiontwo.series[0].data = result.in;

					} else {
						// 登录失败，显示错误消息
						uni.showToast({
							title: result.msg,
							icon: 'none'
						});
					}
				}).catch((error) => {
					console.log('请求失败', error);
				});
			},
		}
	};
</script>
<style>
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