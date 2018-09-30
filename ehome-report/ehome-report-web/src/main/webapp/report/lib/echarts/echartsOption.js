var optionData = {
	line: {
		title: {
			text: '主标题',
			subtext: '副标题'
		},
		legend: {
			data: ['图例']
		},
		xAxis: [{
			type: 'category',
			//			boundaryGap: flag,
			data: ['字段', '字段', '字段', '字段', '字段', '字段', '字段']
		}],
		yAxis: [{
			type: 'value'
				//          axisLabel : {
				//          formatter: '{value} °C'
				//          }
		}],
		tooltip: {
			trigger: 'axis'
		},
		toolbox: {
			show: true,
			feature: {
				saveAsImage: {
					show: true
				}
			}
		},
		calculable: false,
		series: [{
			type: 'line',
			data: [],
			markLine: {
				data: [{
					type: 'average',
					name: '平均值'
				}]
			}
		}]
	},
	bar: {
		title: {
			text: '主标题',
			subtext: '副标题'
		},
		legend: {
			data: ['图例']
		},
		xAxis: [{
			type: 'category',
			//			boundaryGap: flag,
			data: ['字段', '字段', '字段', '字段', '字段', '字段', '字段']
		}],
		yAxis: [{
			type: 'value'
				//          axisLabel : {
				//          formatter: '{value} °C'
				//          }
		}],
		tooltip: {
			trigger: 'axis'
		},
		toolbox: {
			show: true,
			feature: {
				saveAsImage: {
					show: true
				}
			}
		},
		calculable: false,
		series: [{
			type: 'bar',
			data: [],
			markLine: {
				data: [{
					type: 'average',
					name: '平均值'
				}]
			}
		}]
	},
	k: {
		title: {
			text: '主标题',
			subtext: '副标题'
		},
		legend: {
			data: ['图例']
		},
		xAxis: [{
			type: 'category',
			boundaryGap : true,
	        axisTick: {onGap:false},
	        splitLine: {show:false},
			data: ['字段', '字段', '字段', '字段', '字段', '字段', '字段',
				'字段', '字段', '字段', '字段', '字段', '字段', '字段',
				'字段', '字段', '字段', '字段', '字段', '字段', '字段'
			]
		}],
		yAxis: [{
			type: 'value',
			scale: true,
			boundaryGap: [0.01, 0.01]
		}],
		tooltip: {
			trigger: 'axis'
		},
		toolbox: {
			show: true,
			feature: {
				saveAsImage: {
					show: true
				}
			}
		},
		dataZoom: {
			show: true,
			realtime: true,
			start: 50,
			end: 100
		},
		series: [{
			name:'',
			type: 'k',
			data: [
				[0,0,0,0]
			]
		}]
	},
	pie: {
		title: {
			text: '主标题',
			subtext: '副标题',
			x: 'center'
		},
		tooltip: {
			trigger: 'item',
			formatter: "{a} <br/>{b} : {c} ({d}%)"
		},
		legend: {
			orient: 'vertical',
			x: 'left',
			data: ['图例']
		},
		toolbox: {
			show: true,
			feature: {
				saveAsImage: {
					show: true
				}
			}
		},
		calculable: true,
		series: [{
			type: 'pie',
			radius: '55%',
			center: ['50%', '60%'],
			data: [{}]
		}]
	},
	radar: {
		title: {
			text: '主标题',
			subtext: '副标题'
		},
		tooltip: {
			trigger: 'axis'
		},
		legend: {
			orient: 'vertical',
			x: 'right',
			y: 'bottom',
			data: ['图例']
		},
		toolbox: {
			show: true,
			feature: {
				saveAsImage: {
					show: true
				}
			}
		},
		polar: [{
			indicator: [{
				text: '字段',
				max: 100
			}, {
				text: '字段',
				max: 100
			}, {
				text: '字段',
				max: 100
			}, {
				text: '字段',
				max: 100
			}, {
				text: '字段',
				max: 100
			}, {
				text: '字段',
				max: 100
			}]
		}],
		calculable: true,
		series: [{
			name: '',
			type: 'radar',
			data: [{
				value: [],
				name: '字段 '
			}, {
				value: [],
				name: '字段 '
			}]
		}]
	},
	chord: {
		title: {
			text: '主标题',
			subtext: '副标题',
			x: 'right',
			y: 'bottom'
		},
		tooltip: {
			trigger: 'item',
			formatter: function(params) {
				if (params.indicator2) { // is edge
					return params.value.weight;
				} else { // is node
					return params.name
				}
			}
		},
		legend: {
			x: 'left',
			data: ['图例']
		},
		toolbox: {
			show: true,
			feature: {
				saveAsImage: {
					show: true
				}
			}
		},
		series: [{
			type: 'chord',
			sort: 'ascending',
			sortSub: 'descending',
			showScale: true,
			showScaleText: true,
			data: [{
				name: 'group1'
			}, {
				name: 'group2'
			}, {
				name: 'group3'
			}, {
				name: 'group4'
			}],
			itemStyle: {
				normal: {
					label: {
						show: false
					}
				}
			},
			matrix: [
				[11975, 5871, 8916, 2868],
				[1951, 10048, 2060, 6171],
				[8010, 16145, 8090, 8045],
				[1013, 990, 940, 6907]
			]
		}]
	},
	gauge: {
		title: {
			text: '主标题',
			subtext: '副标题',
			x: 'left'
		},
		tooltip: {
			formatter: "{a} <br/>{b} : {c}%"
		},
		toolbox: {
			show: true,
			feature: {
				saveAsImage: {
					show: true
				}
			}
		},
		series: [{
			name: '字段',
			type: 'gauge',
			detail: {
				formatter: '{value}%'
			},
			data: [{value: 50, name: '完成率'}]
		}]
	}
}