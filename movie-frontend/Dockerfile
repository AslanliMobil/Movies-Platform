# 1. Build mərhələsi
FROM node:18-alpine AS build
WORKDIR /app

# Paket fayllarını kopyalayırıq
COPY package*.json ./
RUN npm install

# Bütün kodları kopyalayırıq
COPY . .

# Build komandası (npx ilə ehtiyat variant)
RUN npx vite build

# 2. Nginx mərhələsi (Server)
FROM nginx:stable-alpine
# Vite build nəticəsini 'dist' qovluğuna qoyur
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]