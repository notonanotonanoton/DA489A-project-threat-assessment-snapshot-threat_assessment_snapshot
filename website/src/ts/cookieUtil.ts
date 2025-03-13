export const setCookie = (name: string, value: string, expirationTime: number): void => {
    const date = new Date();
    date.setTime(date.getTime() + (expirationTime * 1000 * 3600));
    const expiration = `expires=${date.toUTCString()}`;
    document.cookie = `${name}=${value}; ${expiration}; path=/; Secure; SameSite=Strict`;
};

export const clearCookie = (name: string): void => {
    document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;`;

};

export const getCookie = (name: string): string | null => {
    const cookieName = `${name}=`;
    const cookies = document.cookie.split(';');

    for (let cookie of cookies){
        cookie = cookie.trim();
        if(cookie.startsWith(cookieName)){
            return cookie.substring(cookieName.length);
        }
    }

    return null;
}