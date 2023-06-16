window["softalks.com/html/form/placeholders"] = {
	message: "Anything but 'Hello, World!', please"
}
window["softalks.com/html/form/titles"] = {
	message: "An imaginative message that will be sent to the system"
}
swap = function() {
	let swap = "http://"
	let dynamic = location.href.includes("/wlpl/")
	if (dynamic) {
		swap += "localhost:9081/static_files/common/intranet/dep/explotacion/arqudesa"
	} else {
		swap += "qilive:9080/wlpl/ADHT-SEGU"
	}
	swap += "/dbac/ui"
	if (dynamic) {
		swap += ".html"
	}
	return swap
}()