#!/bin/sh
if [ -n "$API_BASE_URL" ]; then
    echo "window.APP_CONFIG = { apiBase: '${API_BASE_URL}' };" \
        > /usr/share/nginx/html/js/config.js
fi
exec nginx -g 'daemon off;'
