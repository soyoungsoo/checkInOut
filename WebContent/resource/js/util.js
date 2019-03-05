/**
 * 
 */
function n(n) {
    return n > 9 ? "" + n : "0" + n;
}

function comma(n) {
	return n.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function div(n) {
	return parseInt(n / 60);
}

function rest(n) {
	return parseInt(n % 60);
}