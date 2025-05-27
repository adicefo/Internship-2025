import noImagePlaceholder from '../assets/no_image_placeholder.png';
const getImageSrc = (base64) => {
    if (!base64) return noImagePlaceholder;
    return `data:image/jpeg;base64,${base64}`;
  };

export {getImageSrc};