const toastTrigger = undefined
const toastLiveExample = undefined
const toastBootstrap = undefined

function loadToast() {
    createToast(toastTrigger, toastLiveExample, toastBootstrap)
}
function createToast(trigger, live, toast) {
    trigger = document.getElementById('splitTestSuccess')
    live = document.getElementById('liveToast1')
    if (trigger) {
        toast = bootstrap.Toast.getOrCreateInstance(live)
        trigger.addEventListener('click', () => {
            toast.show()
        })
    }
}