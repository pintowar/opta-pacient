<template>
    <div ref="visualization"></div>
</template>

<script>
import { DataSet, DataView, Timeline } from 'vis'
import 'vis/dist/vis.css'

function showGender (gender) {
    const types = {ANY_GENDER: 'venus-mars', SAME_GENDER: 'transgender', 
                    MALE_ONLY: 'mars', FEMALE_ONLY: 'venus'}
    return `<i class="fas fa-${types[gender]}"></i>`
}

function showEquips (colors) {
    return colors.map(c => `<span class="color-box" style="background-color: ${c};"></span>`).join('')
}

export default {
    name: 'timeline',
    props: {
        groups: {
            type: [Array, DataSet, DataView]
        },
        items: {
            type: [Array, DataSet, DataView]
        }
    },
    data () {
        return {
            options: {
                showMajorLabels: true,
                showCurrentTime: false,
                zoomMin: 1000000,
                zoomKey: 'ctrlKey',
                groupOrder: function (a, b) { return a.departament - b.departament || a.room - b.room || a.bed - b.bed },
                format: {
                    minorLabels: { minute: 'h:mma', hour: 'ha' }
                },
                orientation: { axis: 'top' },
                margin: {item: 1},
                template: function (item) {
                    const gen = item.gender == 'FEMALE' ? 'venus' : 'mars'
                    return `${showEquips(item.colors)} <i class="fas fa-${gen}"></i> ${item.content}`
                },
                groupTemplate: function (item) {
                    const gender = item ? showGender(item.gender) : null
                    const content = (item && item.bed) ? 
                        [item.departament, item.room, item.bed, gender, showEquips(item.colors)]
                        .map(i => `<div class="col">${i}</div>`).join('') : '<div class="col">Unassigned</div>'
                    return `<div class="row">${content}</div>`
                }
            }
        }
    },
    computed: {
        visData () {
            return { items: this.items, groups: this.groups }
        }
    },
    mounted() {
        const container = this.$refs.visualization
        this.timeline = new Timeline(container, this.visData.items, this.visData.groups, this.options)
    },
    watch: {
        visData () {
            const container = this.$refs.visualization
            this.timeline.destroy()
            this.timeline = new Timeline(container, this.visData.items, this.visData.groups, this.options)
        }
    },
    beforeDestroy() {
        this.timeline.destroy()
    }
}
</script>

<style>
.color-box {
    display: inline-block;
    width: 5px;
    height: 10px;
    border: 1px solid rgba(0, 0, 0, .2);
}
div.vis-readonly {
    /* custom styling for readonly items... */
    background-color: rgb(171, 220, 226);
    border-color: rgb(171, 220, 226);
    color: black;
}
</style>