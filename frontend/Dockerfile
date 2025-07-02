# Example Dockerfile for Angular or React app
FROM nginx:alpine
COPY dist/frontend-pgh /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
