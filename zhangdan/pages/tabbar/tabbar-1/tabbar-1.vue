<template>
	<view class="content">
		<view class="calendar">
			<as-calendar ref="as" :markDays="markDays" :headerBar="true" @onDayClick="onDayClick">
				<view slot="header" @click="navigateTo('/pages/tabbar/tabbar-1/myWork')">
					<view class="asTop align-items">
						<text> 日历 </text>
					</view>
				</view>
			</as-calendar>
		</view>
		<view class="billcontent">
			<block v-if="bills.length > 0">
				<view class="bill" v-for="(item, index) in types" :key="index">{{item}}</view>
			</block>
			<ul id="list">
			</ul>
		</view>
		<router-link class="bill" to="/pages/tabbar/tabbar-3/tabbar-3">
			<text class="add">+</text>
		</router-link>
	</view>
</template>

<script>
	import asCalendar from '@/components/as-calendar/as-calendar.vue'

	export default {
		components: {
			asCalendar
		},
		data() {
			return {
				// 日历数据
				curDate: '',
				markDays: ['2022-09-06'],
				bills:[],
			}
		},
		data1() {
			return {
				"email": "",
				"data": "",
			}
		},
		onReady() {
			// 初始化日历
			let today = this.$refs.as.getToday().date
			this.curDate = today
			// this.markDays.push(today)
			console.log(today, 'today')
		},
		methods: {
			getbills(Data) {
				const date1 = {
					email: uni.getStorageSync('email'),
					data: Data
				};

				uni.request({
					url: '/api/getbillsbydata',
					method: 'POST',
					header: {
						'Content-Type': 'application/json'
					},
					data: date1
				}).then((res) => {
					console.log(uni.getStorageSync('email'));
					console.log(uni.getStorageSync('name'));
					console.log(uni.getStorageSync('password'));
					const result = res[1].data;
					console.log(res[1]);
					if (!result) {
						console.error('登录接口返回数据为空');
						console.log(res[1].data);
						console.log(res);
						console.log(result);
						return;
					}
					if (result.code === 0) {
							//this.bills=result.data;
						const contain = document.getElementById("list");
						contain.innerHTML = "";
						// 登录成功
						const Locatdata = result.data;
						const container = document.querySelector('.billcontent');
						// 获取无序列表元素
						const list = document.querySelector('#list');
						// 遍历列表元素并创建对应的列表项，然后添加到无序列表中
						var i = 0;
						Locatdata.forEach(data => {
							// 创建列表项
							const li = document.createElement('li');
							const money = document.createElement('div')
							// 设置列表项内容
							li.textContent = "收支:"+result.data[i].incomeOrExpense+"- - -金额:"+result.data[i].money+"- - -消费类型:"+result.data[i].typeName;
							// 添加列表项到无序列表中
							list.appendChild(li);
							i++;
						});

						// 将无序列表添加到父容器中
						container.appendChild(list);

						// 登录成功，跳转至主页
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
			// 日历点击事件
			onDayClick(data) {
				this.curDate = data.date
				console.log(`选中日期：${this.curDate}`)
				this.getbills(this.curDate);
			}
		}
	}
</script>

<style>
	.content {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 30rpx;
	}

	.asTop {
		display: flex;
		justify-content: space-between;
		align-items: center;
		text-align: start;
		justify-content: space-between;
		height: 80rpx;
		background: linear-gradient(360deg, #ffffff 0%, #e9f8ff 100%);
		padding: 0 30rpx;
		font-size: 30rpx;
		color: #111111;
	}

	.billcontent {
		display: flex;
		justify-content: space-between;
		flex-direction: column;
		align-items: center;
		width: 100%;
		height: auto;
		flex-wrap: wrap;
		border-radius: 15px;
		padding: 10px;
		box-shadow: 0 0 5px rgba(0, 0, 0, 0.8);
		transform: translateY(20px);
	}

	ul#list {
		/* 设置无序列表的样式，如宽度、高度、字体大小等 */
		position: relative;
		justify-content: center;
		align-items: center;
		width: 350px;
		height: auto;
		display: flex;
		justify-content: space-between;
		flex-direction: column;
		align-items: center;
		position: relative;
		list-style-type: none;
		/* 去除列表项的默认标记符号 */
		padding: 0;
		/* 去除列表的内边距 */
	}

	ul#list li{
		width: 350px;
		height: 70px;
		display: flex;
		box-shadow: 0 0 3px rgba(0, 0, 0, 0.8);
		position: relative;
		justify-content: center;
		align-items: center;
		margin-top: 15px;
		border-radius: 10px;
		background-color: #4d7df9;
		background-color: beige;
		color: black;
		margin-top: 100px;
		/* 调整 li 项之间的下间距 */
		/* 设置列表项的样式，如字体颜色、行高、间距等 */
	}

	.billcontent li {
		width: 350px;
		height: 70px;
		display: flex;
		justify-content: center;
		align-items: center;
		margin-top: 15px;
		border-radius: 10px;
		background-color: #4d7df9;
		box-shadow: 0 0 3px rgba(0, 0, 0, 0.8);
		position: relative;
		list-style-type: none;
	}

	.bill {
		width: 350px;
		height: 40px;
		display: flex;
		justify-content: center;
		align-items: center;
		margin-top: 50px;
		border-radius: 10px;
		background-color: #4d7df9;
		box-shadow: 0 0 3px rgba(0, 0, 0, 0.8);
		position: relative;
	}

	.billcontent .bill:first-child {
		margin-top: 0px;
	}

	.money {
		position: absolute;
		font-size: 20px;
		left: 20px;
	}

	.type {
		position: absolute;
		font-size: 20px;
		right: 20px;
	}

	.add {
		position: absolute;
		align-items: center;
		font-size: 30px;
	}
</style>