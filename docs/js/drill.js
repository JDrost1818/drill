$(document).ready(function () {

    const doc_content = $("#doc-content")[0];
    const drawerIcon = $("#drawer-icon")[0];

    $(drawerIcon).click(function () {
        switchDrawerIcon($(this));
    });
    ensureInitialDrawerState(drawerIcon);

    $(".doc-link").each(function (index, elem) {
        $(elem).click(function () {
            changeDocContent(doc_content, $(elem).data("rel"));
            ensureInitialDrawerState(drawerIcon);
        });
    });

    let selectedUrl = getUrlParameter("selected");
    changeDocContent(doc_content, selectedUrl);
});

const supportedPages = ["general", "configuration", "usage"];

/**
 * Changes the doc_content's source to the one provided as long as it is
 * a supported page.
 * @param doc_content
 *          doc_content for which to change source
 * @param relLink
 *          the link to which to change
 */
function changeDocContent(doc_content, relLink) {
    if ($.inArray(relLink, supportedPages) === -1) {
        relLink = "general";
    }
    $(doc_content).load("documentation/" + relLink + ".html", function () {
        loadCards();
        $("#command-line-usage").load("documentation/command-line-usage.html")
    });
}

/**
 * Useful utility function that allows a programmer to extract a
 * url parameter from the current url listing.
 *
 * Found here: https://stackoverflow.com/questions/19491336/get-url-parameter-jquery-or-how-to-get-query-string-values-in-js
 */
function getUrlParameter(sParam) {
    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : sParameterName[1];
        }
    }
}

function ensureInitialDrawerState(drawerIcon) {
    if ($(window).width() <= 600) {
        switchDrawerIcon(drawerIcon, false);
    }
}

function switchDrawerIcon(drawerIcon, shouldBeExpanded) {
    if ($(drawerIcon).hasClass("expanded") || shouldBeExpanded === false) {
        $(drawerIcon).removeClass("expanded");
        $("#side-links-wrapper").removeClass("visible");
        $("#doc-content").removeClass("l8 offset-l3 m7 offset-m4")
    } else if (!$(drawerIcon).hasClass("expanded") || shouldBeExpanded) {
        $(drawerIcon).addClass("expanded");
        $("#side-links-wrapper").addClass("visible");
        $("#doc-content").addClass("l8 offset-l3 m7 offset-m4")
    }
}

function loadCards() {
    $(".card").each(function (i, e) {
        $(this).addClass("z-depth-2 row")
    });
}